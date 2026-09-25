---
name: kotlin-coroutines-flows
description: Design, implement, review, and test Kotlin coroutines and Flow for JVM, Android, and multiplatform targets. Use for scope ownership, cancellation, supervision, dispatchers, concurrent state, Flow operators, retries, sharing, StateFlow, SharedFlow, buffering, delivery guarantees, and deterministic coroutine tests. Apply Android lifecycle integration only to Android code; complement kotlin-coding-standards without prescribing application architecture or Compose UI design.
---

# Kotlin Coroutines and Flow

## Scope and working approach

1. Identify the operation's owner, required lifetime, target platform, failure contract, delivery requirements, and existing coroutine conventions.
2. Inspect the actual APIs: distinguish suspending calls from blocking calls and one-shot results from streams. Check library versions before relying on experimental or platform-specific APIs.
3. Follow `kotlin-coding-standards` when available for general language, mutation, and error policy. Preserve explicit failures by default, permit intentional contract-defined fallbacks, and keep mutable backing state private.
4. Change only the needed coroutine behavior. Do not introduce ViewModels, UI frameworks, dispatchers, or architecture layers solely because an example uses them.
5. Verify affected cancellation, failure, sharing, and lifecycle behavior with focused tests.

## Scope ownership and structured concurrency

- Use `suspend` for one-shot operations and `Flow` for streams. Let callers own operation lifetimes unless a documented requirement says the work must outlive them.
- Use `coroutineScope` for work belonging to one operation. Use an explicitly lifecycle-owned scope for longer-lived work, and define who cancels it.
- Avoid `GlobalScope` and ad hoc detached scopes. Do not add a standalone `Job` or `SupervisorJob` to a child launch when that unintentionally disconnects it from its parent's cancellation.
- Distinguish logical ownership from coroutine job parentage. A ViewModel can belong to an application without `viewModelScope` being a child of an application coroutine scope.
- Use `launch` for owned work without a returned value and `async` for concurrent results. Await `Deferred` results whose outcomes matter; do not use `async` as fire-and-forget error suppression.
- Overlap independent operations when useful. Preserve sequential execution for ordering, simplicity, or resource constraints. Bound concurrency for collections and request batches.
- Remember that concurrent execution does not necessarily mean execution on parallel threads.

```kotlin
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun <A, B> loadTogether(
    first: suspend () -> A,
    second: suspend () -> B,
): Pair<A, B> = coroutineScope {
    val firstResult = async { first() }
    val secondResult = async { second() }
    firstResult.await() to secondResult.await()
}
```

Use a domain-specific result type in application APIs; this pair only illustrates decomposition.

## Failure propagation and supervision

- Use `coroutineScope` when failure of a child should fail the operation and cancel siblings.
- Use `supervisorScope` when sibling operations should survive one another's failures. Give each independently recoverable operation an explicit error policy.
- Do not treat supervision as exception handling. Unhandled exceptions in supervised `launch` children can reach uncaught-exception handling and crash the application.
- Handle recoverable failures inside `launch` children or translate independently awaited results. A `try` around `launch` does not catch a later asynchronous failure from its body.
- Remember that `await()` still throws under supervision. An exception escaping the supervisor block fails the scope and cancels unfinished children.
- Use `CoroutineExceptionHandler` for terminal reporting of otherwise unhandled exceptions where applicable, not for recovering a failed coroutine or replacing local error handling.
- Catch documented operational exceptions. Preserve causes and let unexpected defects propagate according to the application's error boundary.
- Preserve cancellation when converting exceptions to outcomes. Do not use `runCatching` or its catching transformations around suspending work without accounting for their broad `Throwable` capture.

```kotlin
import kotlinx.coroutines.CancellationException

class ServiceUnavailableException(message: String) : Exception(message)

suspend fun <T> serviceResult(load: suspend () -> T): Result<T> =
    try {
        Result.success(load())
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (unavailable: ServiceUnavailableException) {
        Result.failure(unavailable)
    }
```

Use the real service's documented exception type. Introduce a helper only when useful; do not translate every exception into an operational failure.

## Cancellation and resource ownership

- Let cancellation of the current operation propagate. Catch `CancellationException` only for necessary handling followed by rethrowing; never broadly convert it into failure, an empty result, or successful completion.
- For an operation's intentional timeout-as-outcome contract, prefer `withTimeoutOrNull` around that operation rather than broadly swallowing timeout or parent cancellation exceptions.
- Check cancellation in long non-suspending loops with `currentCoroutineContext().ensureActive()`. Use `yield()` when cooperative scheduling is also needed. A `suspend` modifier alone does not make CPU work cancellable.
- Use cancellation-aware adapters for blocking APIs where supported. Do not assume changing dispatcher makes a blocking call interruptible.
- Acquire, use, and release resources within a clear ownership boundary. Use `use` where supported or `try`/`finally`; account for cancellation while acquiring or transferring a resource.
- Restrict `withContext(NonCancellable)` to necessary suspending cleanup. Keep cleanup bounded and do not shield ordinary work from cancellation.
- Do not assume cleanup from an older request may freely overwrite newer state. Serialize work, cancel-and-join previous work, or use request ownership checks where overlapping requests can update the same state.

## Dispatchers and concurrency limits

- Do not assume `suspend` moves work off the caller's thread. Put blocking work on a suitable dispatcher at the implementation boundary.
- Use `Dispatchers.Default` for substantial CPU work and `Dispatchers.IO` for blocking IO on supported targets. Do not automatically switch dispatchers around non-blocking suspending APIs that already manage execution.
- Treat dispatcher support as platform- and dependency-specific. `Dispatchers.IO` is supported on JVM and Native targets; do not describe it as Android-only or assume it exists for every KMP target.
- Check `Dispatchers.Main` availability. JVM needs a suitable Main-dispatcher integration; Native Darwin targets use the main queue, while other Native targets may lack Main. Do not promise a usable Main dispatcher on every target.
- Inject dispatchers or scopes when this improves execution control or testing. Do not use `Default` as a universal workaround for unsupported blocking IO.
- Bound the actual resource being consumed. Dispatcher parallelism limits runnable execution; it is not a mutex and does not necessarily bound the number of suspended requests in flight. Use a semaphore, worker limit, or appropriate concurrency operator when that is the requirement.

## Concurrent state and publication

- Expose `MutableStateFlow` and `MutableSharedFlow` through `asStateFlow()` and `asSharedFlow()`.
- Publish new state values rather than mutating previously published objects or collections. Read-only collection interfaces and shallow copies do not guarantee deep immutability.
- Use `MutableStateFlow.update` for atomic read-modify-write of one flow. Keep the transformation free of side effects because it may be evaluated multiple times under contention.
- Do not treat updates to separate flows as a transaction. Put related invariants in one state value or protect the broader operation explicitly.
- Protect other shared mutable state with confinement or suitable synchronization. Avoid unnecessarily holding a mutex across slow external work; define the needed atomic boundary.
- Treat `StateFlow` as a current-state holder with equality-based conflation. Do not expect every intermediate value or repeated equal event to be delivered.

## Cold flows and operator selection

- Remember that a `flow {}` builder executes for each collection. Multiple collectors can duplicate work unless sharing is intentional.
- Transform an existing Flow directly when no extra builder semantics are needed:

```kotlin
fun observeItems(): Flow<List<Item>> =
    itemDao.observeAll()
        .map { entities -> entities.map { it.toDomain() } }
```

- Use `emitAll` when forwarding another flow inside a builder is actually needed. Do not describe an existing observable database stream as a one-shot-to-stream conversion.
- Use `map` for sequential transformations, `mapLatest` for cancellable latest-value transformations, and `flatMapLatest` when each input selects a Flow. Latest operators require cooperative cancellation to stop underlying work promptly.
- Use `combine` for the latest value from each input; it waits for every input to emit at least once. Do not assume an initial downstream state means upstream inputs have already emitted.
- Use `debounce` when waiting for a quiet period is intended and `distinctUntilChanged` where duplicate inputs should be suppressed. Account for operator order.
- Keep transformations free of unrelated side effects; use explicit handling or `onEach` for intentional effects.
- Use `flowOn` to change upstream execution context. It does not select the downstream collector's dispatcher. Preserve the `flow` builder's context rules; use `channelFlow` for genuinely concurrent producers.
- Use `callbackFlow` for callback adapters when appropriate. Unregister callbacks with `awaitClose` and define buffer/overflow handling instead of silently discarding failed sends.

## Search, recovery, and retry

- Decide when a new input must cancel existing work. Debouncing before a latest operator delays the arrival of that input and therefore delays cancellation; do not promise immediate cancellation with that ordering.
- Do not filter out short or cleared queries before latest-work cancellation if those inputs must cancel an old request or clear old results. Handle them inside the latest operation.
- Keep a failed search distinct from zero matches unless the API deliberately permits an empty fallback. Follow the existing outcome model; changing `Flow<List<Item>>` to a result stream changes the public contract.
- Handle recoverable request failures inside the per-query operation when later inputs must remain usable. Do not swallow cancellation or unexpected defects.
- Remember that Flow `catch` handles upstream exceptions, not downstream collector failures, and preserves cancellation of the flow. A handler that emits a fallback does not resume the failed upstream collection.
- Use bounded retry for appropriate transient failures. Account for idempotency, retry limits, total time budgets, backoff, and jitter where synchronized clients are a concern.
- Remember that retry recollects upstream and can repeat its side effects. Do not retry invalid input or cancellation automatically.

Example for a contract where every changed query requests cancellation of old processing without a debounce delay, valid queries wait briefly, and operational failure is explicit:

```kotlin
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

class SearchUnavailableException(message: String) : Exception(message)

sealed interface SearchState<out T> {
    data class Results<T>(val items: List<T>) : SearchState<T>
    data class Failed(val cause: SearchUnavailableException) : SearchState<Nothing>
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> searchStates(
    queries: Flow<String>,
    search: suspend (String) -> List<T>,
): Flow<SearchState<T>> = queries
    .distinctUntilChanged()
    .mapLatest { query ->
        if (query.length < 2) {
            SearchState.Results<T>(emptyList())
        } else {
            delay(300)
            try {
                SearchState.Results(search(query))
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (unavailable: SearchUnavailableException) {
                SearchState.Failed(unavailable)
            }
        }
    }
```

Treat the exception and outcome types as illustrative contracts. Use actual documented repository failures and project conventions. Consecutive equal queries are suppressed here; remove deduplication or model retry submissions separately when the same query must run again. Cancellation remains cooperative. This example clears results for short queries but does not publish loading or clear old results while a valid query is waiting; add those behaviors explicitly if required. Check operator opt-in requirements against the project's version.

## Sharing, state, and event delivery

- Choose `stateIn` or `shareIn` with an explicit owner, start policy, initial/replay values, and retention policy.
- Use `SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000)` only when a five-second grace period matches the workload. It delays stopping upstream after the last subscriber disappears; it does not itself retain a ViewModel or provide process-death persistence.
- Distinguish stopping upstream from clearing replayed state. Replay expiration is a separate setting; cached values may remain after upstream stops.
- Handle relevant upstream failures before `stateIn` or `shareIn`. An unhandled upstream exception terminates the sharing coroutine and is handled by its scope; it does not automatically become an error value for subscribers.
- Select event transport from delivery requirements. `SharedFlow` broadcasts to active subscribers; it is not an exactly-once event queue.
- Remember that default `MutableSharedFlow()` has no replay or extra buffer. Emissions with no subscribers are lost, and `emit` does not wait for a future subscriber. Extra buffer capacity alone does not retain events in the absence of subscribers.
- Define replay, buffering, overflow, and multiple-subscriber behavior deliberately. Adding replay can redeliver stale actions; it is not a universal event-loss fix.
- Use durable state or explicit pending work and acknowledgement when missing an action is unacceptable. Channels also need cancellation and delivery analysis; receiving an item is not proof its side effect completed.

## Android lifecycle integration

- Apply this section only to Android code. Keep Compose component design, navigation architecture, and package structure in their respective skills.
- Use `viewModelScope` for work owned by a ViewModel, not as a universal scope for every layer. Do not retain an Activity, View, or UI lifecycle owner in a ViewModel.
- Use `collectAsStateWithLifecycle` for Compose UI state where applicable, or `repeatOnLifecycle` for collection that should run only at a chosen lifecycle state. Launch independent collectors as separate children inside a repeat block.
- Remember that `LaunchedEffect` is tied to composition, not directly to foreground lifecycle state. Use appropriate effect keys and lifecycle-aware collection for effects that must stop when the UI is inactive.
- Combine lifecycle-aware collection with an explicit event-loss/replay policy. Stopping collectors can expose gaps in a transient event stream.
- Keep Flow identity stable when reconstruction would unnecessarily restart collection. Prefer stable owners or correctly keyed `remember` where needed; do not blindly cache a flow that depends on changing inputs.

## Deterministic testing

- Use `runTest` and a shared `TestCoroutineScheduler` for participating test dispatchers. Avoid wall-clock sleeps and do not assume virtual time controls work on real dispatchers.
- For local JVM tests using a ViewModel's Main scope, install a test Main dispatcher before creating the ViewModel and restore it afterwards, or inject a suitable scope where the API permits. Reuse the same scheduler.
- Use `runCurrent`, `advanceTimeBy`, and `advanceUntilIdle` according to the behavior under test. Advancing time to a boundary may require `runCurrent` to execute work scheduled exactly there.
- Arrange active collectors when testing lazily started or while-subscribed sharing, even when assertions inspect `StateFlow.value`. Use `backgroundScope` or explicit cancellation for long-lived collectors.
- Use Turbine when emission ordering is part of the contract. Do not assume StateFlow exposes every intermediate loading value: use controlled fakes with suspension gates to make checkpoints observable, or assert current state at meaningful checkpoints.
- Test cancellation propagation, supervised failure handling, resource cleanup, retry limits, short-query cancellation, and recovery on a later query when affected by the change.
- Test no-subscriber and multiple-subscriber event behavior when delivery matters. Test replay expiration and subscription gaps if the start policy is part of the contract.
- Keep fake repositories faithful to ownership and failure semantics. Copy caller-owned lists when needed; exposing a Flow type alone does not protect mutable elements or shared backing storage.
- Match test names to assertions. A final populated state does not prove requests overlapped; use controlled gates or ordering observations when concurrency itself is the behavior under test.

## Final review

- Identify who owns and cancels each coroutine and which failures propagate or recover.
- Check cancellation at broad catches, latest operators, blocking adapters, timeouts, and resource boundaries.
- Check mutable-state ownership, atomicity, and bounded resource use.
- Check stream lifetime after failures and actual event retention/delivery guarantees.
- Check target-specific dispatchers, lifecycle behavior, scheduler setup, and required subscribers in tests.

## References and related skills

- Consult the project's version of the official [coroutine guide](https://kotlinlang.org/docs/coroutines-guide.html), [Flow API](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/), and [coroutine test API](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-test/) for exact behavior and opt-in requirements.
- For Android integration, consult [coroutine testing](https://developer.android.com/kotlin/coroutines/test) and [Flow testing](https://developer.android.com/kotlin/flow/test).
- Use `kotlin-coding-standards` for general Kotlin conventions, mutation boundaries, and error policy when available. Do not redefine those policies here.
- For Android package structure, module boundaries, and layer responsibilities, use `android-clean-architecture` when available.

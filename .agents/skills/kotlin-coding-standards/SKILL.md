---
name: kotlin-coding-standards
description: Write, review, and refactor idiomatic Kotlin for JVM, Android, and multiplatform projects. Use for naming, readability, null safety, data modeling, scoped mutation, API contracts, error handling, collections, extensions, delegation, DSLs, and general testing. Delegate detailed coroutine and Flow mechanics to kotlin-coroutines-flows; keep architecture, Compose UI, framework selection, and build engineering in dedicated skills.
---

# Kotlin Coding Standards

## Code Quality Principles

### 1. Readability First
- Code is read more than written
- Clear variable and function names
- Self-documenting code preferred over comments
- Consistent formatting

### 2. KISS (Keep It Simple, Stupid)
- Simplest solution that works
- Avoid over-engineering
- No premature optimization
- Easy to understand > clever code

### 3. DRY (Don't Repeat Yourself)
- Extract common logic into functions when patterns repeat
- Create reusable components and utilities across modules
- Avoid copy-paste programming
- *Note:* Balance with YAGNI—do not create premature abstractions for single-use cases.

### 4. YAGNI (You Aren't Gonna Need It)
- Don't build features before they're needed
- Avoid speculative generality
- Add complexity only when required
- Start simple, refactor when needed
- *See dedicated skill:* [yagni-principle](file:///Users/jayvijay/Documents/AndroidStudioProjects/GrowTherapy/.agents/skills/yagni-principle/SKILL.md)

## Working approach

1. Inspect relevant code, public contracts, Kotlin version, source sets, and existing conventions before changing code.
2. Preserve behavior and public compatibility unless the task requires a change. Follow the project's formatter, lint rules, and naming conventions.
3. Prefer the simplest clear implementation. Extract genuinely shared behavior; avoid speculative abstractions, needless layers, and unrelated refactoring.
4. Respect existing build tooling and version catalogs. Use Kotlin DSL idioms when editing `.gradle.kts`; do not introduce framework stacks or dependency upgrades merely to match an example.
5. Keep platform-specific APIs in compatible source sets. Verify unfamiliar or version-sensitive behavior against official documentation.
6. Run relevant existing checks and add focused behavioral tests when warranted. Report what was verified and any remaining limitations.

## Readability and naming

- Use descriptive names, action-oriented function names, and clear boolean predicates. Do not reject conventional domain terms merely because they are nouns.
- Prefer expression bodies for short, clear expressions. Use block bodies and named intermediate values when they clarify control flow or side effects.
- Use guard clauses to reduce unnecessary nesting. Keep related operations together and separate distinct logical steps.
- Name literals that encode domain rules, units, limits, or retry policies. Do not replace every obvious literal with a constant.
- Explain rationale, constraints, and surprising behavior in comments. Avoid comments that merely restate code.
- Document public API contracts with KDoc: meaningful absence, validation, failures, ownership, and concurrency expectations where relevant. Avoid redundant prose for self-evident declarations.

## Scoped mutation and ownership

- Prefer `val`. Expose domain data and observable state through read-only properties and interfaces, including `List`, `Map`, `StateFlow`, and `SharedFlow` where appropriate.
- Permit controlled mutation inside implementations and state holders through private backing properties. Expose intent-revealing operations rather than mutable backing state.
- Permit local `var`, local mutable collections, `buildList`, and `buildMap` when they simplify algorithms or iterative construction. Return read-only results and do not leak builder receivers.
- Permit public mutable configuration properties in deliberately scoped builder APIs. Build read-only models and prevent later builder mutations from changing previously built results.
- Distinguish read-only access from immutability: `val` prevents reassignment; a read-only interface restricts operations through that reference. Neither makes the referenced object deeply immutable or thread-safe.
- Copy collections at ownership boundaries when callers or internal code could otherwise mutate shared backing storage. Remember that `toList()` and data-class `copy()` are shallow; mutable elements can still be shared.
- Protect every construction path when an API promises snapshot ownership, including public constructors and copy operations. A builder's defensive copy does not enforce that promise for direct constructor callers.
- Use persistent collections when their guarantees or update costs justify them and the project supports them. Do not add a dependency solely to satisfy terminology.
- Confine shared mutable state or protect it with appropriate synchronization. A private property or copied getter alone does not make concurrent access safe.

```kotlin
data class ServerConfig(val host: String, val tags: List<String>)

class ServerConfigBuilder {
    var host: String = "localhost"
    private val tags = mutableListOf<String>()

    fun tag(value: String) {
        tags += value
    }

    fun build(): ServerConfig {
        require(host.isNotBlank()) { "Host must not be blank" }
        return ServerConfig(host = host, tags = tags.toList())
    }
}
```

This builder isolates its results from subsequent builder mutations. Do not infer that the public `ServerConfig` constructor copies lists supplied by other callers.

## Null safety and type modeling

- Use non-nullable types when absence is invalid. Model meaningful absence with nullable types or an explicit variant.
- Prefer smart casts, safe calls, and Elvis expressions. Use defaults only when they have valid domain meaning; do not fabricate business values to hide missing data.
- Use `requireNotNull` for invalid arguments and `checkNotNull` for invalid internal state. Avoid `!!` and unchecked casts as substitutes for modeling or validation.
- Validate platform types and untrusted values at boundaries. Use `as?` when a failed cast is an expected possibility.
- Use `data class` with `val` properties for value-like records and DTOs. Put properties participating in generated equality and copying in the primary constructor.
- Do not force every domain type into a data class. Use value classes for meaningful wrappers, enums for fixed constants, sealed hierarchies for closed alternatives, and ordinary classes where identity or encapsulated behavior matters.
- Treat value classes as type-safety tools, not a guarantee of zero allocations. Boxing depends on usage and target.
- Use sealed interfaces when variants need only a shared contract; use sealed classes when shared implementation or state is useful.
- Handle closed hierarchies with exhaustive `when` expressions. Avoid an `else` that hides new variants when each variant needs explicit treatment.
- Model mutually exclusive states as alternatives. Use independent fields when their states can legitimately coexist.
- Preserve serialization contracts when editing DTOs. Choose defaults deliberately; do not turn missing required input into apparently valid data.

```kotlin
sealed interface LookupResult<out T> {
    data class Found<T>(val value: T) : LookupResult<T>
    data object Missing : LookupResult<Nothing>
    data class Unavailable(val reason: String) : LookupResult<Nothing>
}
```

Use a distinct name for custom outcome types to avoid confusion with `kotlin.Result`. Include loading only when the contract represents an evolving state rather than a completed operation.

## Error handling and validation

- Make expected failures explicit when callers need to distinguish or handle them. Use nullable returns for meaningful absence, sealed outcomes for distinct domain failures, or `kotlin.Result` for exception-based outcomes, following the API contract.
- Use `require` for argument preconditions, `check` for state invariants, and `error` for unreachable or invalid execution paths. Represent routine business rejection as a domain outcome when it is part of normal operation.
- Catch specific exceptions at boundaries where recovery or translation is possible. Preserve causes. Do not convert programming defects or fatal runtime errors into ordinary success or fallback values.
- Permit fallback values only when the contract intentionally allows degraded behavior. Keep failed searches distinguishable from successful searches with no matches unless the contract explicitly accepts that loss of information.
- Log at an appropriate handling boundary. Avoid duplicate logging in every layer and avoid secrets or sensitive payloads. Logging does not make an inappropriate fallback correct.
- Remember that `runCatching`, `mapCatching`, and `recoverCatching` catch `Throwable`. Use them only when that capture behavior fits the contract; prefer narrow `try`/`catch` when only known operational failures should be converted.

## Essential asynchronous safeguards

- Give asynchronous work an explicit owner and lifetime. Preserve structured cancellation; do not swallow `CancellationException` in general error-handling code.
- Expose observable state through read-only interfaces and avoid mutating already published values.
- Choose concurrency when it provides a concrete benefit, with appropriate resource limits; do not require parallel execution merely because operations are independent.
- Use `kotlin-coroutines-flows` when available for coroutine and Flow implementation, failure propagation, dispatchers, synchronization, stream delivery, lifecycle integration, and testing. That skill specializes these safeguards without replacing the mutation or fallback policies above.

## Functions, extensions, delegation, and DSLs

- Use `let` for scoped transformations, `apply` for receiver configuration, `also` for clear side effects that retain the value, and `run` or `with` for receiver-based computation. Prefer direct code when a scope function adds no clarity.
- Avoid nested scope functions with ambiguous receivers or repeated `it`. Use names or ordinary statements instead.
- Keep extensions cohesive and narrowly visible. Remember that extension resolution is static; do not use extensions to simulate virtual dispatch or hide surprising global behavior.
- Use interface delegation to forward an existing contract without unnecessary inheritance. Override behavior intentionally and preserve the delegated contract.
- Use `lazy` for appropriate deferred initialization and choose its thread-safety mode intentionally. Keep observable mutable delegates within the scoped mutation policy.
- Treat map-backed delegation as access to an already trusted shape; validate untrusted configuration before exposing typed properties.
- Use receiver lambdas and `@DslMarker` when a DSL improves readability. Avoid building a DSL for a simple constructor call.
- Validate builders when building results, protect ownership, and avoid leaking partially configured objects.

## Collections and performance

- Prefer standard operations such as `map`, `filter`, `mapNotNull`, `associateBy`, `groupBy`, and `partition` when they express intent clearly. Use loops when easier to understand.
- Consider empty input, duplicate keys, ordering, and nullable elements. Do not silently overwrite duplicate keys unless that is the intended contract.
- Use sequences for useful lazy evaluation, early termination, or avoiding intermediate allocations. Do not assume a sequence is faster merely because a collection is large.
- Use builders or local mutable buffers for iterative construction. Avoid repeatedly copying a growing list with `list = list + item` in large loops.
- Use `inline` for a concrete purpose, such as reified type parameters or justified higher-order function optimization. Account for generated code growth; do not inline every utility.
- Benchmark consequential performance changes against representative workloads. Do not require benchmarks for routine clear code or claim speedups without evidence.
- Use a monotonic time source for elapsed durations rather than a wall clock that can jump.

## Testing and final review

- Follow the existing test framework and Arrange–Act–Assert structure where useful. Use descriptive names; use backticks where the target and runner support them.
- Test observable behavior, boundary values, validation, and meaningful failures. Avoid tests that merely mirror implementation details.
- Test intentional fallback behavior separately from successful empty results when the API distinguishes them.
- Test snapshot ownership where promised, including later mutation of caller inputs and builders.
- Check that types, names, null handling, and defaults preserve the contract.
- Check mutation boundaries, shared references, error translation, and asynchronous ownership.
- Check that abstractions and optimizations have a concrete purpose, target compatibility is preserved, and relevant verification is complete.

## Related skills

- Use `kotlin-coroutines-flows` for detailed coroutine and Flow behavior and testing when available.
- For Android package structure, module boundaries, and layer responsibilities, use `android-clean-architecture` when available. Keep those decisions in that skill; this skill does not prescribe an Android architecture.

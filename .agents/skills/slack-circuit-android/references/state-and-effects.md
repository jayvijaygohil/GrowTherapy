# State, effects, and presenter recipes

See [state and effects APIs](api/state.md) for producer keys, retention overloads, and effect parameters.

All presenter snippets below belong inside a class's `@Composable override fun present()`. UIs remain `Ui<State>` implementations.

## Retention and ownership

| API | Recomposition | Rotation and back stack | Process recreation |
|---|---|---|---|
| `remember` | Yes | No | No |
| `rememberRetained` | Yes | Yes | No |
| `rememberSaveable` | Yes | Yes | Yes, for saveable values |
| `rememberRetainedSaveable` | Yes | Yes | Yes, for saveable values |

Back-stack survival assumes `NavigableCircuitContent` with its state registries. `CircuitContent` alone has no back stack. Check availability of the combined retained/saveable helper in the installed version. Strings and primitive values are already saveable; custom types need an accepted representation or `Saver`.

Retain values rather than raw Flows, Navigators, Activities, or Contexts. Those objects can capture obsolete lifecycle owners. Retained values survive recreation, but composition-bound coroutine work does not thereby gain the same lifetime.

## Flow and repository observation

Build repository Flows and their operator chains inside a keyed producer:

```kotlin
val messages by produceRetainedState<List<Message>>(emptyList(), screen.channelId) {
  repository.messages(screen.channelId)
    .collect { value = it }
}
```

The producer keeps its result and collects while active. Key it with inputs that should restart collection. `collectAsRetainedState` is appropriate for an already stable Flow reference. Avoid rebuilding operator chains on every recomposition or retaining the raw Flow itself.

## Loading, errors, retry, and refresh

Use sealed state variants when loading, loaded, and failure have mutually exclusive data/events. Keep a loaded state's refresh/error indicator separate when the existing content should remain visible.

A retry event can increment a request key consumed by `produceRetainedState`; changing that key restarts the load. Catch expected failures and map them to state; propagate coroutine cancellation. A refresh may instead call a repository refresh operation while observing its data Flow. Prevent duplicate refresh requests and clear progress on failure as well as success.

Do not assume a retained `isLoading` flag means a cancelled operation is still running. Reconcile progress with the active producer/job when composition restarts.

## Suspend work from events

Use `rememberCoroutineScope()` and launch inside the event sink for cancellable UI work. Update progress before launching if repeated taps must be guarded, and clear progress in `finally`. Do not run suspend work directly in composition or block `present()`.

The scope ends with the composition, including when a screen leaves it or the Activity recreates. Work that must finish after leaving the screen belongs in the data layer's appropriate longer-lived mechanism; the presenter triggers it and observes status.

## Search

```kotlin
var query by rememberRetained { mutableStateOf("") }
val results by produceRetainedState<List<Hit>>(emptyList()) {
  snapshotFlow { query }
    .debounce(300.milliseconds)
    .distinctUntilChanged()
    .mapLatest { text ->
      if (text.isBlank()) emptyList() else repository.search(text)
    }
    .collect { value = it }
}
```

Report query changes through events. Handle loading/error behavior to match the feature. `mapLatest` cancels a previous search when the next debounced query arrives. Use any coroutine API opt-ins required by the installed version.

## Pagination, selection, and forms

- Pagination: retain accumulated items and the next cursor in a state holder, guard concurrent loads, and track initial load separately from load-more progress/error. Append only successful pages; preserve the cursor for retry after failure. Observe scroll thresholds with `snapshotFlow` and send a load-more event instead of calling the repository in the UI. Use stable item keys.
- Shared selection: let the parent own a set of selected IDs and derive each row's selected state. Rows send ID-bearing events; do not create independent selection copies in child presenters.
- Forms: own editable fields and validation state in the presenter or a focused state holder. Derive validity from inputs and show errors based on the feature's interaction policy (for example, touched fields or attempted submit). Keep reusable business validation in injected use cases. Choose saveable state for drafts that must survive process recreation.

## Composition and effects

Split an oversized presenter by responsibility. A parent may invoke injected child presenter instances' `present()` methods, coordinating their states and events. Remember runtime-created child instances with appropriate keys. Extract reusable class-based state producers when they compute state without representing an independent screen; use cases own domain operations. Neither helper requires becoming another navigation destination.

For impression analytics, `circuitx-effects` supplies `ImpressionEffect` and suspendable `LaunchedImpressionEffect`, which run once until forgotten by the retention strategy. `rememberImpressionNavigator` supports re-firing on navigation re-entry. Use these inside the presenter rather than logging directly on each recomposition.

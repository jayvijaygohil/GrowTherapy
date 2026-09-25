# Android navigation and persistence

Use [navigation APIs](api/navigation.md), [host APIs](api/foundation.md), [persistence APIs](api/persistence.md), and [Android destination APIs](api/android.md) for exact declaration lookup.

## Host and stack

Create the stack inside `CircuitCompositionLocals` so it inherits the configured saver. In an Activity's `setContent`:

```kotlin
CircuitCompositionLocals(circuit) {
  val backStack = rememberSaveableBackStack(root = CounterScreen)
  val navigator = rememberCircuitNavigator(backStack) { finish() }
  NavigableCircuitContent(navigator, backStack)
}
```

This connects to the registered classes in [factories](factories.md). The root-pop callback defines behavior when there is no destination to pop. Keep a saveable stack nonempty. Use `CircuitContent(screen)` for a single surface without a navigation stack.

Preserve an existing `BackStack` for push/pop navigation. Use `rememberSaveableNavStack` when the project supports it and needs forward/backward history. `backward()` preserves forward history; `pop()` removes a destination and can return a result; pushing a new screen truncates forward history. For bidirectional navigation, the active/current record can differ from the top record. Decorations should inspect the active record rather than inferring it from size/top.

## Persistence strategy

Recent APIs in the supplied docs make `Screen` and `PopResult` extend `CircuitSaveable`, not Android Parcelable. Older projects may still have Parcelable built into those interfaces. Check the actual version before changing declarations.

Choose one consistent strategy for both screens and results:

| Strategy | Declaration | Setup |
|---|---|---|
| Android Bundle registry | `@Parcelize` with `ParcelableScreen` / `ParcelablePopResult` in versions that provide them | Kotlin Parcelize plugin; default saver from `CircuitCompositionLocals` |
| Reflective serialization | `@Serializable` with `Screen` / `PopResult` | Kotlin serialization plugin, `circuit-serialization-reflect`, `ReflectiveSerializableCircuitSaver()` |
| Generated serialization registrations | `@CircuitSerializable(DiScope::class)` | Serialization plugin, runtime and KSP support; inject registrations into `SerializableCircuitSaver` |
| Manual serialization registrations | `@Serializable` | Serialization runtime and explicitly registered concrete screen/result types |
| Deliberately no persistence | Plain screen/result types | `CircuitSaver.NoOp` |

An ordinary `@Serializable` type alone is not Bundle-saveable. Install a serializing saver, for example with `Circuit.Builder().setCircuitSaver(ReflectiveSerializableCircuitSaver())`. The supplied docs state that the reflective artifact includes its required shrinker rules. Verify restoration in the app's build configuration.

With generated registrations, collect `Set<CircuitSerializerRegistration>` from the graph and construct `SerializableCircuitSaver(registrations)`. Register results as well as screens. Keep this separate from presenter/UI factory contributions.

`CircuitCompositionLocals` uses an explicit saver argument first; otherwise it uses a static saver on Circuit, then an inherited saver or the registry-backed fallback, and applies the builder's configured saver transform. A stack outside these locals needs `ProvideCircuitSaver` or an explicit saver. Create a registry-backed saver in the same saveable-state scope as its stack.

For a gradual format migration, combine savers with `+`, normally registered serialization before the registry fallback and optional reflection last:

```kotlin
Circuit.Builder()
  .setCircuitSaver { fallback -> serializableCircuitSaver + fallback }
  .build()
```

The first saver claiming a value owns the operation even if it returns null or throws. Unsupported saves fail unless deliberately handled with a dropping saver. Keep delegate order and formats stable across recreation; transient feature flags must not change the chain. `NoOp` restores the initial stack, so use it only when losing navigation state is intentional.

Restoration may drop unavailable records or reset the stack if required history cannot be restored. Incomplete stored tab snapshots are discarded; unavailable pending results clear their expectations. Account for removed/changed destinations when changing saved formats.

## Results

Inside a presenter class's `present()`:

```kotlin
var name by rememberSaveable { mutableStateOf("") }
val answeringNavigator = rememberAnsweringNavigator<EditNameResult>(navigator) {
  name = it.name
}
// An event handler calls answeringNavigator.goTo(EditNameScreen).
// The target's event handler calls navigator.pop(EditNameResult(updatedName)).
```

`EditNameResult` implements `PopResult` using the chosen persistence strategy. Navigate with the answering navigator, not its fallback, to register the receiver. `NavigableCircuitContent` delivers results only to the requesting record. A pop without a result does not invoke its callback. Outside that host the helper returns the fallback and cannot deliver results; inspect `answeringNavigationAvailable()` where needed.

Use a screen result for a navigable editor/picker whose request must survive process recreation. Use an overlay for an ephemeral dialog/sheet; its suspended interaction does not survive process death.

## Tabs and nested navigation

A tab-host `Ui` class may own a nested stack, navigator, and `NavigableCircuitContent`. Derive the selected tab from the stack root and switch with `navigator.resetRoot(tab.rootScreen, Navigator.StateOptions.SaveAndRestore)`. This saves the outgoing tab's stack and restores the target's stored stack, or starts at its root on first visit. Avoid a redundant selected-tab field or separate stacks unless the product requires different behavior.

`StateOptions` flags: `save` saves the outgoing stack; `restore` restores the target's stored stack; `clear` removes the target's stored snapshot after restoration. Defaults are false.

For nested `CircuitContent` that delegates navigation, forward `onNavEvent` through the parent state's event sink and handle it with `navigator.onNavEvent(event)`. For embedded components that are not destinations, consider the class-based SubCircuit guidance in [UI integration](ui-integration.md).

## Android destinations and deep links

Decorate the host navigator with `rememberAndroidScreenAwareNavigator(baseNavigator, context)` from `circuitx-android`, and pass the decorated instance to the content host. Presenter events can navigate to an `IntentScreen`. The Context overload supports Intent screens; custom `AndroidScreen` types need an `AndroidScreenStarter`.

For deep links, map accepted URI routes and arguments to a nonempty screen history, configure the manifest entry point, and define the expected Back destination. Verify the installed stack constructor's list ordering rather than assuming root-first/top-first ordering. Handle both cold start and new intents delivered to an existing Activity. In the supplied API snapshot, changing `root` or `initialScreens` creates a new remembered stack; changing `navStackList` likewise recreates a remembered NavStack. Treat this as stack replacement, not incremental navigation, and keep initialization inputs stable when preserving running history. Preserve restoration behavior and avoid reapplying a consumed launch intent on rotation.

## Interception

Use `rememberInterceptingNavigator` from `circuitx-navigation` for ordered authentication gates, destination rewrites, Android handoff, or navigation observation. Results distinguish skipped, consumed success, failure, and rewrite. In newer APIs a rewrite contains a `NavEvent`, such as `InterceptedResult.Rewrite(NavEvent.GoTo(target))`.

The supplied docs mix older operation-specific result types with newer unified `InterceptedResult` and `NavigationContext` signatures. Inspect the installed interface before implementing it. Keep rewrites bounded to avoid routing loops. Event listeners observe successful base navigation; they are not a complete record of intercepted actions. Use the interceptor/failure notifier for those paths. For simple Android intent handoff, the Android-aware navigator alone is sufficient.

# Android setup and dependency injection

Use [core annotations](api/core.md), [builder APIs](api/foundation.md), and [serialization APIs](api/persistence.md) when wiring dependencies and factory sets.

Use the project's version catalog and align Circuit artifacts to its chosen version. Do not pin the documentation's apparent version or add every extension module. Configure Android Compose support using the project's existing Kotlin/AGP conventions.

| Need | Artifact under `com.slack.circuit` |
|---|---|
| Circuit, content hosts, core APIs | `circuit-foundation` |
| Presenter-only feature API | `circuit-runtime-presenter` |
| UI-only feature API | `circuit-runtime-ui` |
| Shared runtime contracts | `circuit-runtime` |
| Retained state helpers | `circuit-retained` |
| Presenter/navigation testing | `circuit-test` |
| Overlays / ready-made sheets and dialogs | `circuit-overlay` / `circuitx-overlays` |
| Android destinations | `circuitx-android` |
| Interceptors / impression effects | `circuitx-navigation` / `circuitx-effects` |
| Gesture navigation / shared transitions | `circuitx-gesture-navigation` / `circuit-sharedelements` |
| Serialization-based navigation persistence | `circuit-serialization` or `circuit-serialization-reflect` |

`circuit-foundation` includes core dependencies; feature modules can depend more narrowly where their architecture requires it. Add extensions only when used.

## Class-based code generation

Manual factories need no processor. For generated factories, add the KSP plugin and:

```kotlin
dependencies {
  implementation("com.slack.circuit:circuit-codegen-annotations:$circuitVersion")
  ksp("com.slack.circuit:circuit-codegen:$circuitVersion")
}
```

`circuitVersion` represents the project's resolved version. Expose annotations with `api` only if the module's public API needs that visibility.

Set `circuit.codegen.mode` to the existing DI system: `metro`, `hilt`, or `kotlin_inject_anvil`. The supplied docs also describe a legacy Dagger/Anvil default; do not rely on that default for a different DI framework or introduce a DI migration as part of an ordinary feature.

```kotlin
ksp {
  arg("circuit.codegen.mode", "hilt")
}
```

Annotate injectable `Presenter` and `Ui` classes with `@CircuitInject(ScreenType::class, ScopeType::class)` to generate their factory contributions. The scope must match the DI framework, such as the appropriate Hilt component. Use the annotation/import conventions of that DI system. An annotated class must actually be injectable.

For Dagger/Hilt assisted injection, annotate the assisted factory rather than the enclosing presenter class:

```kotlin
class DetailPresenter @AssistedInject constructor(
  @Assisted private val screen: DetailScreen,
  @Assisted private val navigator: Navigator,
  private val repository: DetailRepository,
) : Presenter<DetailState> {
  @Composable
  override fun present(): DetailState {
    // Observe repository data and return the feature's state.
    val title by produceRetainedState("", screen.id) {
      repository.observeTitle(screen.id).collect { value = it }
    }
    return DetailState(title)
  }

  @CircuitInject(DetailScreen::class, SingletonComponent::class)
  @AssistedFactory
  interface Factory {
    fun create(screen: DetailScreen, navigator: Navigator): DetailPresenter
  }
}
```

Here `DetailScreen`, `DetailRepository`, and `DetailState` are feature-owned types. The assisted factory parameters must exactly match the constructor's assisted parameters. Do not copy an extra `CircuitContext` argument into the assisted factory just because the outer `Presenter.Factory.create` receives one. Check the processor's supported assisted types before adding runtime arguments; the supplied documentation lists screen, navigator (presenters only), and Circuit.

For a UI with ordinary injected dependencies, annotate the injectable `Ui<State>` class. Collect generated `Presenter.Factory` and `Ui.Factory` multibindings in the application graph and add them to the Circuit builder; generating contributions alone does not create the app's Circuit instance.

Metro and kotlin-inject have different injection annotation and assisted-creation conventions. Follow existing project examples and inspect generated sources after KSP rather than copying Dagger annotations into those graphs. For kotlin-inject-anvil, the supplied docs require `CircuitInject` and, when used, `CircuitSerializable` in `kotlin-inject-anvil-contributing-annotations` as colon-separated fully qualified names.

Qualifiers on an annotated declaration propagate to generated factories. Preserve intentional qualifiers when collecting factory sets.

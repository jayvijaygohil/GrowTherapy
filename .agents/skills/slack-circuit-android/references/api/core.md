# API: Screen, presenter, UI, and factory contracts

Use [the API index](../api-index.md) for snapshot limitations. Package and owner names appear in each heading. Signatures preserve documented parameter names, defaults, and types; whitespace is normalized. Class declarations and abstract methods are reference signatures, not standalone implementations.

## Contract notes

The factory `create` return types are nullable so unmatched screens can fall through to other factories. `ParcelableScreen` and `ParcelablePopResult` include Android `Parcelable`; ordinary `Screen` and `PopResult` do not. `StaticScreen` identifies content that can run without a presenter.

## circuit-runtime-presenter / com.slack.circuit.runtime.presenter / Presenter

Downloaded source: `circuit-runtime-presenter/com.slack.circuit.runtime.presenter/-presenter/index.html`

```kotlin
@Stable interface Presenter < UiState : CircuitUiState >
```

## circuit-runtime-presenter / com.slack.circuit.runtime.presenter / Presenter / present

Downloaded source: `circuit-runtime-presenter/com.slack.circuit.runtime.presenter/-presenter/present.html`

```kotlin
@Composable abstract fun present () : UiState
```

## circuit-runtime-presenter / com.slack.circuit.runtime.presenter / Presenter / Factory

Downloaded source: `circuit-runtime-presenter/com.slack.circuit.runtime.presenter/-presenter/-factory/index.html`

```kotlin
@Stable fun interface Factory
```

## circuit-runtime-presenter / com.slack.circuit.runtime.presenter / Presenter / Factory / create

Downloaded source: `circuit-runtime-presenter/com.slack.circuit.runtime.presenter/-presenter/-factory/create.html`

```kotlin
abstract fun create (screen : Screen, navigator : Navigator, context : CircuitContext) : Presenter < * > ?
```

## circuit-runtime-ui / com.slack.circuit.runtime.ui / Ui

Downloaded source: `circuit-runtime-ui/com.slack.circuit.runtime.ui/-ui/index.html`

```kotlin
@Stable interface Ui < UiState : CircuitUiState >
```

## circuit-runtime-ui / com.slack.circuit.runtime.ui / Ui / Content

Downloaded source: `circuit-runtime-ui/com.slack.circuit.runtime.ui/-ui/-content.html`

```kotlin
@Composable abstract fun Content (state : UiState, modifier : Modifier)
```

## circuit-runtime-ui / com.slack.circuit.runtime.ui / Ui / Factory

Downloaded source: `circuit-runtime-ui/com.slack.circuit.runtime.ui/-ui/-factory/index.html`

```kotlin
@Stable fun interface Factory
```

## circuit-runtime-ui / com.slack.circuit.runtime.ui / Ui / Factory / create

Downloaded source: `circuit-runtime-ui/com.slack.circuit.runtime.ui/-ui/-factory/create.html`

```kotlin
abstract fun create (screen : Screen, context : CircuitContext) : Ui < * > ?
```

## circuit-runtime / com.slack.circuit.runtime / CircuitUiState

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-circuit-ui-state/index.html`

```kotlin
@Stable interface CircuitUiState
```

## circuit-runtime / com.slack.circuit.runtime / CircuitUiEvent

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-circuit-ui-event/index.html`

```kotlin
@Immutable interface CircuitUiEvent
```

## circuit-runtime / com.slack.circuit.runtime / CircuitContext

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-circuit-context/index.html`

```kotlin
@Stable class CircuitContext (val parent : CircuitContext ?, tags : MutableMap < KClass < * >, Any > = mutableMapOf())
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / Screen

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-screen/index.html`

```kotlin
@Immutable interface Screen : CircuitSaveable
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / StaticScreen

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-static-screen/index.html`

```kotlin
@Immutable interface StaticScreen : Screen
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / ParcelableScreen

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-parcelable-screen/index.html`

```kotlin
@Immutable interface ParcelableScreen : Screen, Parcelable
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / PopResult

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-pop-result/index.html`

```kotlin
@Immutable interface PopResult : CircuitSaveable
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / ParcelablePopResult

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-parcelable-pop-result/index.html`

```kotlin
@Immutable interface ParcelablePopResult : PopResult, Parcelable
```

## circuit-codegen-annotations / com.slack.circuit.codegen.annotations / CircuitInject

Downloaded source: `circuit-codegen-annotations/com.slack.circuit.codegen.annotations/-circuit-inject/index.html`

```kotlin
@Target (allowedTargets = [ AnnotationTarget.CLASS, AnnotationTarget.FUNCTION ]) annotation class CircuitInject
```

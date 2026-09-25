# API: Circuit builder and content hosts

Use [the API index](../api-index.md) for snapshot limitations. Package and owner names appear in each heading. Signatures preserve documented parameter names, defaults, and types; whitespace is normalized. Class declarations and abstract methods are reference signatures, not standalone implementations.

## Contract notes

`CircuitContent` is a composition host, not a function-based presenter/UI registration pattern. Use factory resolution for production feature wiring. `NavigableCircuitContent` parameter names distinguish `navStack` from `backStack`; `onRootPop` receives an optional result.

## circuit-foundation / com.slack.circuit.foundation / Circuit / Builder / addPresenterFactory

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-circuit/-builder/add-presenter-factory.html`

```kotlin
fun addPresenterFactory (factory : Presenter.Factory) : Circuit.Builder

fun addPresenterFactory (vararg factory : Presenter.Factory) : Circuit.Builder
```

## circuit-foundation / com.slack.circuit.foundation / Circuit / Builder / addPresenterFactories

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-circuit/-builder/add-presenter-factories.html`

```kotlin
fun addPresenterFactories (factories : Iterable < Presenter.Factory >) : Circuit.Builder
```

## circuit-foundation / com.slack.circuit.foundation / Circuit / Builder / addUiFactory

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-circuit/-builder/add-ui-factory.html`

```kotlin
fun addUiFactory (factory : Ui.Factory) : Circuit.Builder

fun addUiFactory (vararg factory : Ui.Factory) : Circuit.Builder
```

## circuit-foundation / com.slack.circuit.foundation / Circuit / Builder / addUiFactories

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-circuit/-builder/add-ui-factories.html`

```kotlin
fun addUiFactories (factories : Iterable < Ui.Factory >) : Circuit.Builder
```

## circuit-foundation / com.slack.circuit.foundation / Circuit / Builder / build

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-circuit/-builder/build.html`

```kotlin
fun build () : Circuit
```

## circuit-foundation / com.slack.circuit.foundation / Circuit / Builder / presentWithLifecycle

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-circuit/-builder/present-with-lifecycle.html`

```kotlin
fun presentWithLifecycle (enable : Boolean = true) : Circuit.Builder

var presentWithLifecycle : Boolean
```

## circuit-foundation / com.slack.circuit.foundation / Circuit / Builder / setCircuitSaver

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-circuit/-builder/set-circuit-saver.html`

```kotlin
fun setCircuitSaver (circuitSaver : CircuitSaver ?) : Circuit.Builder

fun setCircuitSaver (transform : (CircuitSaver) -> CircuitSaver) : Circuit.Builder
```

## circuit-foundation / com.slack.circuit.foundation / CircuitCompositionLocals

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-circuit-composition-locals.html`

```kotlin
@Composable fun CircuitCompositionLocals (circuit : Circuit, retainedStateRegistry : RetainedStateRegistry = lifecycleRetainedStateRegistry(), content : @Composable () -> Unit)

@Composable fun CircuitCompositionLocals (circuit : Circuit, circuitSaver : CircuitSaver, retainedStateRegistry : RetainedStateRegistry = lifecycleRetainedStateRegistry(), content : @Composable () -> Unit)
```

## circuit-foundation / com.slack.circuit.foundation / CircuitContent

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-circuit-content.html`

```kotlin
@Composable fun CircuitContent (screen : Screen, modifier : Modifier = Modifier, circuit : Circuit = requireNotNull(LocalCircuit.current), unavailableContent : @Composable (screen : Screen, modifier : Modifier) -> Unit = circuit.onUnavailableContent, key : Any ? = screen)

@Composable fun CircuitContent (screen : Screen, modifier : Modifier = Modifier, onNavEvent : (event : NavEvent) -> Unit, circuit : Circuit = requireNotNull(LocalCircuit.current), unavailableContent : @Composable (screen : Screen, modifier : Modifier) -> Unit = circuit.onUnavailableContent, key : Any ? = screen)

@Composable fun CircuitContent (screen : Screen, navigator : Navigator, modifier : Modifier = Modifier, circuit : Circuit = requireNotNull(LocalCircuit.current), unavailableContent : @Composable (screen : Screen, modifier : Modifier) -> Unit = circuit.onUnavailableContent, key : Any ? = screen)

@Composable fun < UiState : CircuitUiState > CircuitContent (screen : Screen, presenter : Presenter < UiState >, ui : Ui < UiState >, modifier : Modifier = Modifier, eventListener : EventListener = EventListener.NONE, key : Any ? = screen, presentWithLifecycle : Boolean = LocalCircuit.current?.presentWithLifecycle == true)
```

## circuit-foundation / com.slack.circuit.foundation / NavigableCircuitContent

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/-navigable-circuit-content.html`

```kotlin
@Composable fun < R : NavStack.Record > NavigableCircuitContent (navigator : Navigator, navStack : NavStack < R >, modifier : Modifier = Modifier, circuit : Circuit = requireNotNull(LocalCircuit.current), providedValues : Map < out NavStack.Record, ProvidedValues > = emptyMap(), decoration : NavDecoration = circuit.defaultNavDecoration, decoratorFactory : AnimatedNavDecorator.Factory ? = null, unavailableRoute : @Composable (screen : Screen, modifier : Modifier) -> Unit = circuit.onUnavailableContent)

@Composable fun < R : BackStack.Record > NavigableCircuitContent (navigator : Navigator, backStack : BackStack < R >, modifier : Modifier = Modifier, circuit : Circuit = requireNotNull(LocalCircuit.current), providedValues : Map < out NavStack.Record, ProvidedValues > = emptyMap(), decoration : NavDecoration = circuit.defaultNavDecoration, decoratorFactory : AnimatedNavDecorator.Factory ? = null, unavailableRoute : @Composable (screen : Screen, modifier : Modifier) -> Unit = circuit.onUnavailableContent)

@Composable fun < R : NavStack.Record > NavigableCircuitContent (navigator : AnsweringResultNavigator < R >, modifier : Modifier = Modifier, circuit : Circuit = requireNotNull(LocalCircuit.current), providedValues : Map < out NavStack.Record, ProvidedValues > = emptyMap(), decoration : NavDecoration = circuit.defaultNavDecoration, decoratorFactory : AnimatedNavDecorator.Factory ? = null, unavailableRoute : @Composable (screen : Screen, modifier : Modifier) -> Unit = circuit.onUnavailableContent)
```

## circuit-foundation / com.slack.circuit.foundation / rememberCircuitNavigator

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/remember-circuit-navigator.html`

```kotlin
@Composable fun rememberCircuitNavigator (backStack : BackStack < out BackStack.Record >, enableBackHandler : Boolean = true) : Navigator

@Composable fun rememberCircuitNavigator (navStack : NavStack < out NavStack.Record >, enableBackHandler : Boolean = true) : Navigator

@Composable fun rememberCircuitNavigator (backStack : BackStack < out BackStack.Record >, onRootPop : (result : PopResult ?) -> Unit) : Navigator

@Composable fun rememberCircuitNavigator (navStack : NavStack < out NavStack.Record >, onRootPop : (result : PopResult ?) -> Unit) : Navigator

@Composable fun rememberCircuitNavigator (backStack : BackStack < out BackStack.Record >, onRootPop : (result : PopResult ?) -> Unit, enableBackHandler : Boolean = true) : Navigator

@Composable fun rememberCircuitNavigator (navStack : NavStack < out NavStack.Record >, onRootPop : (result : PopResult ?) -> Unit, enableBackHandler : Boolean = true) : Navigator
```

## circuit-foundation / com.slack.circuit.foundation / rememberDefaultCircuitSaver

Downloaded source: `circuit-foundation/com.slack.circuit.foundation/remember-default-circuit-saver.html`

```kotlin
@Composable fun rememberDefaultCircuitSaver () : CircuitSaver
```

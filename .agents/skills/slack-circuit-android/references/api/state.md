# API: Retained state and impression effects

Use [the API index](../api-index.md) for snapshot limitations. Package and owner names appear in each heading. Signatures preserve documented parameter names, defaults, and types; whitespace is normalized. Class declarations and abstract methods are reference signatures, not standalone implementations.

## Contract notes

Producer keys restart production. Retention keeps the value, not an indefinitely running collection. The `saver` overload saves an entire retained value; `stateSaver` saves the value inside `MutableState`. Saved state can lag behind in-memory updates made after Android collects instance state.

## circuit-retained / com.slack.circuit.retained / rememberRetained

Downloaded source: `circuit-retained/com.slack.circuit.retained/remember-retained.html`

```kotlin
@Composable fun < T : Any > rememberRetained (vararg inputs : Any ?, init : () -> T) : T

@Composable fun < T : Any > rememberRetained (vararg inputs : Any ?, saver : Saver < T, out Any >, key : String ? = null, init : () -> T) : T

@Composable fun < T > rememberRetained (vararg inputs : Any ?, stateSaver : Saver < T, out Any >, key : String ? = null, init : () -> MutableState < T >) : MutableState < T >
```

## circuit-retained / com.slack.circuit.retained / rememberRetainedSaveable

Downloaded source: `circuit-retained/com.slack.circuit.retained/remember-retained-saveable.html`

```kotlin
@Composable fun < T : Any > rememberRetainedSaveable (vararg inputs : Any ?, saver : Saver < T, out Any > = autoSaver(), key : String ? = null, init : () -> T) : T

@Composable fun < T > rememberRetainedSaveable (vararg inputs : Any ?, stateSaver : Saver < T, out Any >, key : String ? = null, init : () -> MutableState < T >) : MutableState < T >
```

## circuit-retained / com.slack.circuit.retained / produceRetainedState

Downloaded source: `circuit-retained/com.slack.circuit.retained/produce-retained-state.html`

```kotlin
@Composable fun < T > produceRetainedState (initialValue : T, producer : suspend ProduceStateScope < T >.() -> Unit) : State < T >

@Composable fun < T > produceRetainedState (initialValue : T, key1 : Any ?, producer : suspend ProduceStateScope < T >.() -> Unit) : State < T >

@Composable fun < T > produceRetainedState (initialValue : T, key1 : Any ?, key2 : Any ?, producer : suspend ProduceStateScope < T >.() -> Unit) : State < T >

@Composable fun < T > produceRetainedState (initialValue : T, key1 : Any ?, key2 : Any ?, key3 : Any ?, producer : suspend ProduceStateScope < T >.() -> Unit) : State < T >

@Composable fun < T > produceRetainedState (initialValue : T, vararg keys : Any ?, producer : suspend ProduceStateScope < T >.() -> Unit) : State < T >
```

## circuit-retained / com.slack.circuit.retained / collectAsRetainedState

Downloaded source: `circuit-retained/com.slack.circuit.retained/collect-as-retained-state.html`

```kotlin
@Composable fun < T > StateFlow < T >.collectAsRetainedState (context : CoroutineContext = EmptyCoroutineContext) : State < T >

@Composable fun < T : R, R > Flow < T >.collectAsRetainedState (initial : R, context : CoroutineContext = EmptyCoroutineContext) : State < R >
```

## circuit-retained / com.slack.circuit.retained / produceAndCollectAsRetainedState

Downloaded source: `circuit-retained/com.slack.circuit.retained/produce-and-collect-as-retained-state.html`

```kotlin
@Composable fun < T : R, R > produceAndCollectAsRetainedState (vararg inputs : Any ?, initial : R, context : CoroutineContext = EmptyCoroutineContext, producer : suspend () -> Flow < T >) : State < R >
```

## circuitx/effects / com.slack.circuitx.effects / ImpressionEffect

Downloaded source: `circuitx/effects/com.slack.circuitx.effects/-impression-effect.html`

```kotlin
@Composable fun ImpressionEffect (vararg inputs : Any ?, impression : () -> Unit)
```

## circuitx/effects / com.slack.circuitx.effects / LaunchedImpressionEffect

Downloaded source: `circuitx/effects/com.slack.circuitx.effects/-launched-impression-effect.html`

```kotlin
@Composable fun LaunchedImpressionEffect (vararg inputs : Any ?, impression : suspend () -> Unit)
```

## circuitx/effects / com.slack.circuitx.effects / rememberImpressionNavigator

Downloaded source: `circuitx/effects/com.slack.circuitx.effects/remember-impression-navigator.html`

```kotlin
@Composable fun rememberImpressionNavigator (vararg inputs : Any ?, navigator : Navigator, impression : suspend () -> Unit) : Navigator
```

# API: Presenter and event testing

Use [the API index](../api-index.md) for snapshot limitations. Package and owner names appear in each heading. Signatures preserve documented parameter names, defaults, and types; whitespace is normalized. Class declarations and abstract methods are reference signatures, not standalone implementations.

## Contract notes

`awaitNextScreen` returns the screen; `awaitNextGoTo` returns a navigation event. Use `awaitPop` and `awaitResetRoot` to inspect those operations rather than expecting presenter state emissions for navigation-only actions. `Presenter.test` accepts composition locals in its second overload.

## circuit-test / com.slack.circuit.test / test

Downloaded source: `circuit-test/com.slack.circuit.test/test.html`

```kotlin
suspend fun < UiState : CircuitUiState > Presenter < UiState >.test (timeout : Duration ? = null, name : String ? = null, policy : SnapshotMutationPolicy < UiState > = structuralEqualityPolicy(), block : suspend CircuitReceiveTurbine < UiState >.() -> Unit)

suspend fun < UiState : CircuitUiState > Presenter < UiState >.test (vararg locals : ProvidedValue < * >, timeout : Duration ? = null, name : String ? = null, policy : SnapshotMutationPolicy < UiState > = structuralEqualityPolicy(), block : suspend CircuitReceiveTurbine < UiState >.() -> Unit)
```

## circuit-test / com.slack.circuit.test / FakeNavigator / FakeNavigator

Downloaded source: `circuit-test/com.slack.circuit.test/-fake-navigator/-fake-navigator.html`

```kotlin
constructor (navStack : NavStack < out NavStack.Record >)

constructor (root : Screen, vararg additionalScreens : Screen)
```

## circuit-test / com.slack.circuit.test / FakeNavigator / awaitNextScreen

Downloaded source: `circuit-test/com.slack.circuit.test/-fake-navigator/await-next-screen.html`

```kotlin
suspend fun awaitNextScreen () : Screen
```

## circuit-test / com.slack.circuit.test / FakeNavigator / awaitNextGoTo

Downloaded source: `circuit-test/com.slack.circuit.test/-fake-navigator/await-next-go-to.html`

```kotlin
suspend fun awaitNextGoTo () : FakeNavigator.GoToEvent
```

## circuit-test / com.slack.circuit.test / FakeNavigator / awaitPop

Downloaded source: `circuit-test/com.slack.circuit.test/-fake-navigator/await-pop.html`

```kotlin
suspend fun awaitPop () : FakeNavigator.PopEvent
```

## circuit-test / com.slack.circuit.test / FakeNavigator / awaitResetRoot

Downloaded source: `circuit-test/com.slack.circuit.test/-fake-navigator/await-reset-root.html`

```kotlin
suspend fun awaitResetRoot () : FakeNavigator.ResetRootEvent
```

## circuit-test / com.slack.circuit.test / FakeNavigator / expectNoGoToEvents

Downloaded source: `circuit-test/com.slack.circuit.test/-fake-navigator/expect-no-go-to-events.html`

```kotlin
fun expectNoGoToEvents ()
```

## circuit-test / com.slack.circuit.test / FakeNavigator / expectNoPopEvents

Downloaded source: `circuit-test/com.slack.circuit.test/-fake-navigator/expect-no-pop-events.html`

```kotlin
fun expectNoPopEvents ()
```

## circuit-test / com.slack.circuit.test / FakeNavigator / expectNoResetRootEvents

Downloaded source: `circuit-test/com.slack.circuit.test/-fake-navigator/expect-no-reset-root-events.html`

```kotlin
fun expectNoResetRootEvents ()
```

## circuit-test / com.slack.circuit.test / TestEventSink / TestEventSink

Downloaded source: `circuit-test/com.slack.circuit.test/-test-event-sink/-test-event-sink.html`

```kotlin
constructor ()
```

## circuit-test / com.slack.circuit.test / TestEventSink / assertEvent

Downloaded source: `circuit-test/com.slack.circuit.test/-test-event-sink/assert-event.html`

```kotlin
fun assertEvent (event : UiEvent) : TestEventSink < UiEvent >

fun assertEvent (predicate : (UiEvent) -> Boolean) : TestEventSink < UiEvent >
```

## circuit-test / com.slack.circuit.test / TestEventSink / assertEvents

Downloaded source: `circuit-test/com.slack.circuit.test/-test-event-sink/assert-events.html`

```kotlin
fun assertEvents (vararg events : UiEvent) : TestEventSink < UiEvent >

fun assertEvents (predicate : (Int, UiEvent) -> Boolean) : TestEventSink < UiEvent >
```

## circuit-test / com.slack.circuit.test / TestEventSink / assertNoEvents

Downloaded source: `circuit-test/com.slack.circuit.test/-test-event-sink/assert-no-events.html`

```kotlin
fun assertNoEvents () : TestEventSink < UiEvent >
```

## circuit-test / com.slack.circuit.test / CircuitReceiveTurbine / awaitUnchanged

Downloaded source: `circuit-test/com.slack.circuit.test/-circuit-receive-turbine/await-unchanged.html`

```kotlin
abstract suspend fun awaitUnchanged ()
```

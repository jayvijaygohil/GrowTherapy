# Factories and feature structure

[Core API contracts](api/core.md) provide factory signatures and screen/state types.

A `Screen` is an immutable key and arguments for a presenter/UI pair. Pass identifiers and small navigation inputs; load domain data in the presenter. State implements `CircuitUiState`; events normally implement `CircuitUiEvent`. The UI renders state and reports events through its `eventSink`.

## Complete manual factory example

The following example uses Android Parcelable persistence with the newer `ParcelableScreen` interface. For a project where `Screen` itself extends Parcelable, use its existing declaration style. See [navigation persistence](navigation.md) for serialization alternatives. Imports are omitted; resolve them from the project's Circuit version.

```kotlin
@Parcelize
data object CounterScreen : ParcelableScreen

data class CounterState(
  val count: Int,
  val eventSink: (CounterEvent) -> Unit,
) : CircuitUiState

sealed interface CounterEvent : CircuitUiEvent {
  data object Increment : CounterEvent
  data object Back : CounterEvent
}

class CounterPresenter(
  private val navigator: Navigator,
) : Presenter<CounterState> {
  @Composable
  override fun present(): CounterState {
    var count by rememberSaveable { mutableIntStateOf(0) }
    return CounterState(count) { event ->
      when (event) {
        CounterEvent.Increment -> count++
        CounterEvent.Back -> navigator.pop()
      }
    }
  }

  class Factory : Presenter.Factory {
    override fun create(
      screen: Screen,
      navigator: Navigator,
      context: CircuitContext,
    ): Presenter<*>? = when (screen) {
      CounterScreen -> CounterPresenter(navigator)
      else -> null
    }
  }
}

class CounterUi : Ui<CounterState> {
  @Composable
  override fun Content(state: CounterState, modifier: Modifier) {
    Column(modifier = modifier) {
      Text(text = state.count.toString())
      Button(onClick = { state.eventSink(CounterEvent.Increment) }) {
        Text("Increment")
      }
      Button(onClick = { state.eventSink(CounterEvent.Back) }) {
        Text("Back")
      }
    }
  }

  class Factory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? =
      when (screen) {
        CounterScreen -> CounterUi()
        else -> null
      }
  }
}

val circuit = Circuit.Builder()
  .addPresenterFactory(CounterPresenter.Factory())
  .addUiFactory(CounterUi.Factory())
  .build()
```

Keep composition-owned state in `present()` or `Content()`, not mutable class fields. Presenter constructor fields hold dependencies and runtime arguments. Never emit Compose UI from `present()`; its composable target is presentation logic. Pass the incoming `Modifier` to the UI root.

Factories receive `CircuitContext` for resolution context. A presenter factory additionally receives the current `Navigator`; forward that navigator rather than constructing another one. Factories for parameterized screens should type-check with `is DetailScreen` and pass the matched screen to the constructor.

Aggregate factories in the application's existing DI graph and install them in `Circuit.Builder`. Avoid registering both a manual factory and a generated factory for the same implementation. Keep overlapping factory matching deliberate, respecting the builder's resolution order in the installed version.

A `StaticScreen` can render without a presenter when it needs no computed state. It still needs a class-based UI factory; follow the installed version's static-state contract rather than inventing a presenter requirement.

For isolated previews and UI tests, call `CounterUi().Content(state, Modifier)` inside composition. No presenter or navigation host is required for rendering supplied state.

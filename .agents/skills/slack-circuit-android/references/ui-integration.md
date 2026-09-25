# Overlays and UI integration

Use [overlay APIs](api/overlays.md) and [Android gesture APIs](api/android.md) for the supplied snapshot. See [coverage gaps](api-index.md) for shared elements and SubCircuit.

## Dialogs and sheets

Add `circuit-overlay`; add `circuitx-overlays` for its ready-made dialog/sheet implementations. Wrap the content host with `ContentWithOverlays` to provide `LocalOverlayHost` and `LocalOverlayState`.

Model the pending request in presenter state. `null` means hidden; a value identifies the request. The UI class hosts the overlay and returns the answer as an event:

```kotlin
class ItemUi : Ui<ItemState> {
  @Composable
  override fun Content(state: ItemState, modifier: Modifier) {
    val pendingId = state.pendingDelete
    if (pendingId != null) {
      OverlayEffect(pendingId) {
        val result = show(
          alertDialogOverlay(
            title = { Text("Delete this item?") },
            confirmButton = { onClick ->
              Button(onClick = onClick) { Text("Delete") }
            },
            dismissButton = { onClick ->
              TextButton(onClick = onClick) { Text("Cancel") }
            },
          )
        )
        state.eventSink(ItemEvent.DeleteAnswered(pendingId, result == DialogResult.Confirm))
      }
    }
    Box(modifier = modifier) {
      // Render the feature's item content and emit its events.
    }
  }
}
```

The presenter sets the request in response to an event and clears it for confirm, cancel, or dismiss. Guard against applying a stale answer to a different pending request. Business operations stay in the presenter/data layer; do not inject `OverlayHost` just to perform a confirmation from presenter logic.

`Overlay<Result>` implements `@Composable Content(OverlayNavigator<Result>)`. An overlay finishes with `overlayNavigator.finish(result)` rather than navigating the app. `OverlayHost.show()` suspends until a typed result is available. A `BottomSheetOverlay(model) { model, overlayNavigator -> ... }` can return a selected value in the same way. The supplied API has two sheet constructors: without `onDismiss`, outside taps do not dismiss the sheet and only its content finishes it; with `onDismiss: () -> Result`, outside dismissal produces that result. Choose deliberately and map cancellation explicitly.

Overlays do not participate in the app's back stack and their suspended calls do not survive process recreation. A saved pending-request value may cause a new overlay to be shown after recreation; that is a new interaction, not restoration of the old coroutine.

## Shared elements and gestures

For `circuit-sharedelements`, place `SharedElementTransitionLayout` outside both `ContentWithOverlays` and `NavigableCircuitContent`. In a `Ui.Content` implementation, enter `SharedElementTransitionScope`, use stable matching content keys, and obtain the navigation or overlay animated scope with `requireAnimatedScope` or `findAnimatedScope`. Use the optional lookup when that scope can legitimately be absent. Previews can use `PreviewSharedElementTransitionLayout`.

`circuitx-gesture-navigation` provides `GestureNavigationDecorationFactory(fallback, listener)` in the supplied API snapshot. Pass the resulting factory as the host's `decoratorFactory`; navigation drives the pop. The listener observes gesture progress, cancellation, and completion and must not pop again. Older narrative examples use `onBackInvoked`, which is absent from this API snapshot; match the installed dependency instead. Verify Android predictive-back behavior on the target API levels and ordinary back behavior on older devices.

## Existing Android Views

Host Circuit inside `ComposeView` when embedding it in a View hierarchy, with an appropriate composition disposal strategy. For an existing View inside a Circuit UI class, use `AndroidView` in `Content`, pass its `modifier`, and update View state/listeners from the current Circuit state in `update`.

## Embedded components and SubCircuit

Use an ordinary child composable for rendering-only reuse. A nested independently stateful presenter/UI pair that delegates actions to its parent may fit experimental `circuitx-subcircuit`. Check availability and opt-ins first; do not add it merely to render a row.

The class-based structure is:

- `SubScreen<OuterEvent>` identifies the component and inputs.
- `SubPresenter<OuterEvent, State>` implements `@Composable present(outerEventSink)`.
- `SubUi<State>` implements its composable content contract; state implements `SubCircuitUiState`.
- `SubPresenterFactory` and `SubUiFactory` resolve the implementations. Inspect their actual generic signatures in the dependency before writing manual factories.
- `SubCircuit.builder()` aggregates those factories; provide the result through `LocalSubCircuit`.
- The parent's `Ui.Content` renders `SubCircuitContent` and maps outer events into its own state event sink. Navigation stays with the parent presenter.

The supplied docs show `@SubCircuitInject` on assisted presenter factories, but document UI code generation using annotated functions. For this skill use an explicit `SubUi` class and manual UI factory unless class-based generation is confirmed in the installed processor. Do not assume `@CircuitInject` class support implies identical SubCircuit codegen support.

Use `circuitx-subcircuit-test` to assert emitted state and `outerEvents.awaitEvent()` when testing a SubPresenter.

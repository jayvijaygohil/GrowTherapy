# API: Overlays, dialogs, and sheets

Use [the API index](../api-index.md) for snapshot limitations. Package and owner names appear in each heading. Signatures preserve documented parameter names, defaults, and types; whitespace is normalized. Class declarations and abstract methods are reference signatures, not standalone implementations.

## Contract notes

`OverlayEffect` runs its block as a keyed effect when an overlay host is available; otherwise it renders `fallback` if supplied. `BottomSheetOverlay` without `onDismiss` does not dismiss on an outside tap. The constructor with `onDismiss` uses its returned result for outside dismissal. `DialogResult` has `Confirm`, `Cancel`, and `Dismiss` values. `showFullScreenOverlay` occupies the current overlay host's available space.

## circuit-overlay / com.slack.circuit.overlay / Overlay

Downloaded source: `circuit-overlay/com.slack.circuit.overlay/-overlay/index.html`

```kotlin
@Stable fun interface Overlay < Result : Any >
```

## circuit-overlay / com.slack.circuit.overlay / Overlay / Content

Downloaded source: `circuit-overlay/com.slack.circuit.overlay/-overlay/-content.html`

```kotlin
@Composable abstract fun Content (navigator : OverlayNavigator < Result >)
```

## circuit-overlay / com.slack.circuit.overlay / OverlayHost / show

Downloaded source: `circuit-overlay/com.slack.circuit.overlay/-overlay-host/show.html`

```kotlin
abstract suspend fun < Result : Any > show (overlay : Overlay < Result >) : Result
```

## circuit-overlay / com.slack.circuit.overlay / OverlayNavigator / finish

Downloaded source: `circuit-overlay/com.slack.circuit.overlay/-overlay-navigator/finish.html`

```kotlin
abstract fun finish (result : Result)
```

## circuit-overlay / com.slack.circuit.overlay / ContentWithOverlays

Downloaded source: `circuit-overlay/com.slack.circuit.overlay/-content-with-overlays.html`

```kotlin
@Composable fun ContentWithOverlays (modifier : Modifier = Modifier, overlayHost : OverlayHost = rememberOverlayHost(), content : @Composable () -> Unit)
```

## circuit-overlay / com.slack.circuit.overlay / OverlayEffect

Downloaded source: `circuit-overlay/com.slack.circuit.overlay/-overlay-effect.html`

```kotlin
@Composable fun OverlayEffect (vararg keys : Any ?, fallback : @Composable () -> Unit ? = null, block : suspend OverlayScope.() -> Unit)
```

## circuit-overlay / com.slack.circuit.overlay / LocalOverlayHost

Downloaded source: `circuit-overlay/com.slack.circuit.overlay/-local-overlay-host.html`

```kotlin
val LocalOverlayHost : ProvidableCompositionLocal < OverlayHost >
```

## circuit-overlay / com.slack.circuit.overlay / LocalOverlayState

Downloaded source: `circuit-overlay/com.slack.circuit.overlay/-local-overlay-state.html`

```kotlin
val LocalOverlayState : ProvidableCompositionLocal < OverlayState >
```

## circuitx/overlays / com.slack.circuitx.overlays / alertDialogOverlay

Downloaded source: `circuitx/overlays/com.slack.circuitx.overlays/alert-dialog-overlay.html`

```kotlin
fun alertDialogOverlay (confirmButton : @Composable (OnClick) -> Unit, icon : @Composable () -> Unit ? = null, title : @Composable () -> Unit ? = null, text : @Composable () -> Unit ? = null, dismissButton : @Composable (OnClick) -> Unit ?, properties : DialogProperties = DialogProperties()) : BasicAlertDialogOverlay < *, DialogResult >
```

## circuitx/overlays / com.slack.circuitx.overlays / BottomSheetOverlay / BottomSheetOverlay

Downloaded source: `circuitx/overlays/com.slack.circuitx.overlays/-bottom-sheet-overlay/-bottom-sheet-overlay.html`

```kotlin
constructor (model : Model, sheetContainerColor : Color ? = null, tonalElevation : Dp ? = null, sheetShape : Shape ? = null, dragHandle : @Composable () -> Unit ? = null, skipPartiallyExpandedState : Boolean = false, isFocusable : Boolean = true, contentWindowInsets : @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets }, content : @Composable (Model, OverlayNavigator < Result >) -> Unit)

constructor (model : Model, onDismiss : () -> Result, sheetContainerColor : Color ? = null, tonalElevation : Dp ? = null, sheetShape : Shape ? = null, dragHandle : @Composable () -> Unit ? = null, skipPartiallyExpandedState : Boolean = false, properties : ModalBottomSheetProperties = DEFAULT_PROPERTIES, contentWindowInsets : @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets }, content : @Composable (Model, OverlayNavigator < Result >) -> Unit)
```

## circuitx/overlays / com.slack.circuitx.overlays / DialogResult

Downloaded source: `circuitx/overlays/com.slack.circuitx.overlays/-dialog-result/index.html`

```kotlin
enum DialogResult : Enum < DialogResult >
```

## circuitx/overlays / com.slack.circuitx.overlays / showFullScreenOverlay

Downloaded source: `circuitx/overlays/com.slack.circuitx.overlays/show-full-screen-overlay.html`

```kotlin
suspend fun OverlayHost.showFullScreenOverlay (screen : Screen) : PopResult ?
```

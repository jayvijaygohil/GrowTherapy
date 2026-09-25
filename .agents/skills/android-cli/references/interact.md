# Inspecting and interacting with a device

## Command and device context

Check `android layout --help`, `android screen capture --help`, and `android screen resolve --help` for the installed CLI. Examples below use CLI `1.0.16406183`. Replace `<serial>` with the selected device serial for both Android CLI and ADB commands.

## Hierarchy inspection

```sh
android layout --device=<serial> --pretty
android layout --device=<serial> --flat
```

Default output is a tree; `--flat` requests a flat list. In the checked version, `--diff` is deprecated and does nothing. Obtain a fresh layout after state changes; filter relevant nodes or compare saved snapshots when only changes matter. Use `--full` when hidden or non-interactive elements are needed, but do not mistake hidden/off-screen nodes for visible controls.

Depending on the version, element data can include text, resource ID, content description, interactions, state, bounds, center, and off-screen status. Inspect actual JSON before writing a parser. Use supported interactions and current bounds to select a visible target. A screenshot is useful when a WebView, animation, image, or custom drawing is missing from the hierarchy.

## Screenshots and coordinates

```sh
android screen capture --device=<serial> --output=screen.png
android screen capture --device=<serial> --annotate --output=annotated.png
android screen resolve --screenshot=annotated.png --string='#3'
```

Visually inspect the captured PNG before relying on its contents or selecting a labeled region. `--annotate` belongs to `screen capture`; the checked `screen resolve` command requires `--screenshot`, not `--screen`.

Resolve a label from the exact image you inspected. Check the resulting numeric coordinates, then send a separate input command. Do not execute resolver output as shell code. Recapture after navigation, scrolling, rotation, or layout movement rather than reusing stale labels or bounds.

## Input

For an observed button centered at `(152, 23)`:

```sh
adb -s <serial> shell input tap 152 23
```

For an observed scrollable region spanning `[100,200][400,600]`, an example upward gesture is:

```sh
adb -s <serial> shell input swipe 250 400 250 220 500
```

Choose coordinates inside the actual region and a duration appropriate to the gesture. Inspect the resulting state before another action. Scroll to search for controls when the task permits it; journey assertions about current visibility do not permit scrolling to make them pass.

Before entering text, focus the intended field and verify focus through layout state or visible evidence when hierarchy data is unavailable. For simple ASCII text:

```sh
adb -s <serial> shell input text Hello%sworld
adb -s <serial> shell input keyevent 66
```

`%s` represents a space in this input method. Submit with Enter only when the task requires it. Arbitrary text crosses both host and device shell parsing; do not interpolate it into shell commands or assume host quotes alone protect metacharacters. Use a tested escaping/input method appropriate to the text, and verify the displayed value. This method is not a general Unicode text transport.

After a transition, allow a bounded wait for loading and inspect again. If inspection fails, retry once after checking device connectivity or use a screenshot. If reliable observation is still unavailable, report the tool failure instead of guessing the app state.

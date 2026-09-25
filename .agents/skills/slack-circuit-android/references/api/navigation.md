# API: Navigation, stacks, and results

Use [the API index](../api-index.md) for snapshot limitations. Package and owner names appear in each heading. Signatures preserve documented parameter names, defaults, and types; whitespace is normalized. Class declarations and abstract methods are reference signatures, not standalone implementations.

## Contract notes

The documented remember-stack functions create a new stack if their root/initial list changes. Initial lists must be nonempty. `NavStack.currentRecord` identifies the active destination; `topRecord` can differ when forward history exists. Answering navigation requires a host that provides result delivery.

## circuit-runtime / com.slack.circuit.runtime / Navigator / goTo

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-navigator/go-to.html`

```kotlin
abstract override fun goTo (screen : Screen) : Boolean
```

## circuit-runtime / com.slack.circuit.runtime / Navigator / pop

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-navigator/pop.html`

```kotlin
abstract fun pop (result : PopResult ? = null) : Screen ?
```

## circuit-runtime / com.slack.circuit.runtime / Navigator / resetRoot

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-navigator/reset-root.html`

```kotlin
abstract fun resetRoot (newRoot : Screen, options : Navigator.StateOptions = StateOptions.Default) : List < Screen >
```

## circuit-runtime / com.slack.circuit.runtime / Navigator / peek

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-navigator/peek.html`

```kotlin
abstract fun peek () : Screen ?
```

## circuit-runtime / com.slack.circuit.runtime / Navigator / peekNavStack

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-navigator/peek-nav-stack.html`

```kotlin
abstract fun peekNavStack () : NavStackList < Screen > ?
```

## circuit-runtime / com.slack.circuit.runtime / Navigator / forward

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-navigator/forward.html`

```kotlin
abstract fun forward () : Boolean
```

## circuit-runtime / com.slack.circuit.runtime / Navigator / backward

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-navigator/backward.html`

```kotlin
abstract fun backward () : Boolean
```

## circuit-runtime / com.slack.circuit.runtime / Navigator / StateOptions / StateOptions

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-navigator/-state-options/-state-options.html`

```kotlin
constructor (save : Boolean = false, restore : Boolean = false, clear : Boolean = false)
```

## circuit-runtime / com.slack.circuit.runtime / Navigator / StateOptions / Companion / SaveAndRestore

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/-navigator/-state-options/-companion/-save-and-restore.html`

```kotlin
val SaveAndRestore : Navigator.StateOptions
```

## circuit-runtime / com.slack.circuit.runtime / rememberAnsweringNavigator

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/remember-answering-navigator.html`

```kotlin
@Composable inline fun < T : PopResult > rememberAnsweringNavigator (fallbackNavigator : Navigator, noinline block : (result : T) -> Unit) : GoToNavigator

@Composable fun < T : PopResult > rememberAnsweringNavigator (fallbackNavigator : Navigator, resultType : KClass < T >, block : (result : T) -> Unit) : GoToNavigator
```

## circuit-runtime / com.slack.circuit.runtime / answeringNavigationAvailable

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/answering-navigation-available.html`

```kotlin
@Composable fun answeringNavigationAvailable () : Boolean
```

## circuit-runtime / com.slack.circuit.runtime / popUntil

Downloaded source: `circuit-runtime/com.slack.circuit.runtime/pop-until.html`

```kotlin
fun Navigator.popUntil (predicate : (Screen) -> Boolean)
```

## backstack / com.slack.circuit.backstack / rememberSaveableBackStack

Downloaded source: `backstack/com.slack.circuit.backstack/remember-saveable-back-stack.html`

```kotlin
@Composable fun rememberSaveableBackStack (root : Screen, circuitSaver : CircuitSaver = LocalCircuitSaver.current, init : SaveableBackStack.() -> Unit = {}) : SaveableBackStack

@Composable fun rememberSaveableBackStack (initialScreens : List < Screen >, circuitSaver : CircuitSaver = LocalCircuitSaver.current) : SaveableBackStack
```

## backstack / com.slack.circuit.backstack / isAtRoot

Downloaded source: `backstack/com.slack.circuit.backstack/is-at-root.html`

```kotlin
val BackStack < out BackStack.Record >.isAtRoot : Boolean
```

## circuit-foundation / com.slack.circuit.foundation.navstack / rememberSaveableNavStack

Downloaded source: `circuit-foundation/com.slack.circuit.foundation.navstack/remember-saveable-nav-stack.html`

```kotlin
@Composable fun rememberSaveableNavStack (root : Screen, circuitSaver : CircuitSaver = LocalCircuitSaver.current, init : SaveableNavStack.() -> Unit = {}) : NavStack < out NavStack.Record >

@Composable fun rememberSaveableNavStack (initialScreens : List < Screen >, circuitSaver : CircuitSaver = LocalCircuitSaver.current) : NavStack < out NavStack.Record >

@Composable fun rememberSaveableNavStack (navStackList : NavStackList < Screen >, circuitSaver : CircuitSaver = LocalCircuitSaver.current) : NavStack < out NavStack.Record >
```

## circuit-foundation / com.slack.circuit.foundation.navstack / SaveableNavStack / currentRecord

Downloaded source: `circuit-foundation/com.slack.circuit.foundation.navstack/-saveable-nav-stack/current-record.html`

```kotlin
open override val currentRecord : SaveableNavStack.Record ?
```

## circuit-foundation / com.slack.circuit.foundation.navstack / SaveableNavStack / rootRecord

Downloaded source: `circuit-foundation/com.slack.circuit.foundation.navstack/-saveable-nav-stack/root-record.html`

```kotlin
open override val rootRecord : SaveableNavStack.Record ?
```

## circuit-foundation / com.slack.circuit.foundation.navstack / SaveableNavStack / topRecord

Downloaded source: `circuit-foundation/com.slack.circuit.foundation.navstack/-saveable-nav-stack/top-record.html`

```kotlin
open override val topRecord : SaveableNavStack.Record ?
```

## circuit-foundation / com.slack.circuit.foundation.navstack / SaveableNavStack / snapshot

Downloaded source: `circuit-foundation/com.slack.circuit.foundation.navstack/-saveable-nav-stack/snapshot.html`

```kotlin
open override fun snapshot () : NavStackList < SaveableNavStack.Record > ?
```

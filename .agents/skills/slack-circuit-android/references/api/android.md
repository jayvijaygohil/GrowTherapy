# API: Android destinations and gesture navigation

Use [the API index](../api-index.md) for snapshot limitations. Package and owner names appear in each heading. Signatures preserve documented parameter names, defaults, and types; whitespace is normalized. Class declarations and abstract methods are reference signatures, not standalone implementations.

## Contract notes

The Context overload of the Android-aware navigator handles `IntentScreen`; a custom starter handles custom Android screens. Gesture listener callbacks only observe navigation: do not pop again in `onBackCompleted`. The supplied gesture factory takes `fallback` and `listener`, not `onBackInvoked`.

## circuitx/android / com.slack.circuitx.android / rememberAndroidScreenAwareNavigator

Downloaded source: `circuitx/android/com.slack.circuitx.android/remember-android-screen-aware-navigator.html`

```kotlin
@CheckResult @Composable fun rememberAndroidScreenAwareNavigator (delegate : Navigator, context : Context) : Navigator

@CheckResult @Composable fun rememberAndroidScreenAwareNavigator (delegate : Navigator, starter : AndroidScreenStarter) : Navigator
```

## circuitx/android / com.slack.circuitx.android / AndroidScreen

Downloaded source: `circuitx/android/com.slack.circuitx.android/-android-screen/index.html`

```kotlin
interface AndroidScreen : Screen
```

## circuitx/android / com.slack.circuitx.android / AndroidScreenStarter

Downloaded source: `circuitx/android/com.slack.circuitx.android/-android-screen-starter/index.html`

```kotlin
fun interface AndroidScreenStarter
```

## circuitx/android / com.slack.circuitx.android / AndroidScreenStarter / start

Downloaded source: `circuitx/android/com.slack.circuitx.android/-android-screen-starter/start.html`

```kotlin
abstract fun start (screen : AndroidScreen) : Boolean
```

## circuitx/android / com.slack.circuitx.android / IntentScreen / IntentScreen

Downloaded source: `circuitx/android/com.slack.circuitx.android/-intent-screen/-intent-screen.html`

```kotlin
constructor (intent : Intent, options : Bundle ? = null)
```

## circuitx/gesture-navigation / com.slack.circuitx.gesturenavigation / GestureNavigationDecorationFactory

Downloaded source: `circuitx/gesture-navigation/com.slack.circuitx.gesturenavigation/-gesture-navigation-decoration-factory.html`

```kotlin
fun GestureNavigationDecorationFactory (fallback : AnimatedNavDecorator.Factory, listener : GestureNavigationEventListener) : AnimatedNavDecorator.Factory

fun GestureNavigationDecorationFactory (fallback : AnimatedNavDecorator.Factory = NavigatorDefaults.DefaultDecoratorFactory, listener : GestureNavigationEventListener = GestureNavigationEventListener.NoOp) : AnimatedNavDecorator.Factory
```

## circuitx/gesture-navigation / com.slack.circuitx.gesturenavigation / GestureNavigationEventListener

Downloaded source: `circuitx/gesture-navigation/com.slack.circuitx.gesturenavigation/-gesture-navigation-event-listener/index.html`

```kotlin
interface GestureNavigationEventListener
```

## circuitx/gesture-navigation / com.slack.circuitx.gesturenavigation / GestureNavigationEventListener / onBackCancelled

Downloaded source: `circuitx/gesture-navigation/com.slack.circuitx.gesturenavigation/-gesture-navigation-event-listener/on-back-cancelled.html`

```kotlin
open fun onBackCancelled ()
```

## circuitx/gesture-navigation / com.slack.circuitx.gesturenavigation / GestureNavigationEventListener / onBackCompleted

Downloaded source: `circuitx/gesture-navigation/com.slack.circuitx.gesturenavigation/-gesture-navigation-event-listener/on-back-completed.html`

```kotlin
open fun onBackCompleted ()
```

## circuitx/gesture-navigation / com.slack.circuitx.gesturenavigation / GestureNavigationEventListener / onBackProgress

Downloaded source: `circuitx/gesture-navigation/com.slack.circuitx.gesturenavigation/-gesture-navigation-event-listener/on-back-progress.html`

```kotlin
open fun onBackProgress (progress : Float)
```

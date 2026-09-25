# API: Navigation persistence

Use [the API index](../api-index.md) for snapshot limitations. Package and owner names appear in each heading. Signatures preserve documented parameter names, defaults, and types; whitespace is normalized. Class declarations and abstract methods are reference signatures, not standalone implementations.

## circuit-runtime-screen / com.slack.circuit.runtime.screen / CircuitSaveable

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-circuit-saveable/index.html`

```kotlin
interface CircuitSaveable
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / CircuitSaver

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-circuit-saver/index.html`

```kotlin
@Stable abstract class CircuitSaver
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / CircuitSaver / save

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-circuit-saver/save.html`

```kotlin
abstract fun save (value : CircuitSaveable) : Any ?
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / CircuitSaver / Companion / NoOp

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-circuit-saver/-companion/-no-op.html`

```kotlin
val NoOp : CircuitSaver
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / CircuitSaver / Companion / Dropping

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-circuit-saver/-companion/-dropping.html`

```kotlin
fun Dropping (onDropped : (CircuitSaveable) -> Unit) : CircuitSaver
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / ProvideCircuitSaver

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/-provide-circuit-saver.html`

```kotlin
@Composable fun ProvideCircuitSaver (circuitSaver : CircuitSaver, content : @Composable () -> Unit)
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / restoreScreen

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/restore-screen.html`

```kotlin
inline fun < T : Screen > CircuitSaver.restoreScreen (saved : Any, onAbsent : () -> Unit = {}, onTypeMismatch : (CircuitSaveable) -> Unit = {
    error("Expected ${T::class}, but CircuitSaver restored ${it::class}.")
  }) : T ?
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / restorePopResult

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/restore-pop-result.html`

```kotlin
inline fun < T : PopResult > CircuitSaver.restorePopResult (saved : Any, onAbsent : () -> Unit = {}, onTypeMismatch : (CircuitSaveable) -> Unit = {
    error("Expected ${T::class}, but CircuitSaver restored ${it::class}.")
  }) : T ?
```

## circuit-runtime-screen / com.slack.circuit.runtime.screen / plus

Downloaded source: `circuit-runtime-screen/com.slack.circuit.runtime.screen/plus.html`

```kotlin
operator fun CircuitSaver.plus (other : CircuitSaver) : CircuitSaver
```

## circuit-serialization / com.slack.circuit.serialization / CircuitSerializable

Downloaded source: `circuit-serialization/com.slack.circuit.serialization/-circuit-serializable/index.html`

```kotlin
@MetaSerializable @Target (allowedTargets = [ AnnotationTarget.CLASS ]) annotation class CircuitSerializable (val scope : KClass < * >)
```

## circuit-serialization / com.slack.circuit.serialization / SerializableCircuitSaver

Downloaded source: `circuit-serialization/com.slack.circuit.serialization/-serializable-circuit-saver.html`

```kotlin
fun SerializableCircuitSaver (configuration : SavedStateConfiguration = SavedStateConfiguration.DEFAULT, onRestoreError : (Throwable) -> Unit = {}) : CircuitSaver

fun SerializableCircuitSaver (registrations : Iterable < CircuitSerializerRegistration >, configuration : SavedStateConfiguration = SavedStateConfiguration.DEFAULT, onRestoreError : (Throwable) -> Unit = {}) : CircuitSaver
```

## circuit-serialization / com.slack.circuit.serialization / CircuitSerializerRegistration

Downloaded source: `circuit-serialization/com.slack.circuit.serialization/-circuit-serializer-registration/index.html`

```kotlin
fun interface CircuitSerializerRegistration
```

## circuit-serialization / com.slack.circuit.serialization / CircuitSerializerRegistration / register

Downloaded source: `circuit-serialization/com.slack.circuit.serialization/-circuit-serializer-registration/register.html`

```kotlin
abstract fun register (builder : PolymorphicModuleBuilder < CircuitSaveable >)
```

## circuit-serialization / com.slack.circuit.serialization / circuitSerializersModule

Downloaded source: `circuit-serialization/com.slack.circuit.serialization/circuit-serializers-module.html`

```kotlin
fun circuitSerializersModule (registrations : Iterable < CircuitSerializerRegistration >) : SerializersModule
```

## circuit-serialization-reflect / com.slack.circuit.serialization.reflect / ReflectiveSerializableCircuitSaver

Downloaded source: `circuit-serialization-reflect/com.slack.circuit.serialization.reflect/-reflective-serializable-circuit-saver.html`

```kotlin
fun ReflectiveSerializableCircuitSaver (configuration : SavedStateConfiguration = SavedStateConfiguration.DEFAULT, onRestoreError : (Throwable) -> Unit = {}) : CircuitSaver
```

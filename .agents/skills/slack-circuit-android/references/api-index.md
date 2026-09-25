# API lookup

Use these references for declaration names, packages, receiver types, parameters, defaults, return types, and overloads. Read the existing task guide for behavior and architecture, then open only the relevant API file. Search by symbol name when the file contains multiple APIs.

## Snapshot and precedence

These are selected declarations extracted from the user-supplied Dokka HTML in `api/`, incorporated on 2026-09-22. The inspected pages have no release version in their library-version field and their source links point to `main`; do not assign this snapshot a released version. A consuming Android project's resolved dependency sources take precedence if its version differs.

The extraction retains APIs callable from Android, removes platform-selection UI and site boilerplate, and omits function-based presenter/UI adapters and registration helpers. Platform declaration keywords have been removed. Some declarations have multiple rendered variants with different defaults; these do not imply extra distinct Android overloads. Use dependency sources to resolve ambiguity. Dokka's rendered signatures are lookup aids, not compilable stubs: they can omit source-level details and render nullable function types ambiguously.

Each entry names its package and owner and records its original downloaded HTML path for traceability. Those paths are provenance identifiers, not runtime dependencies. The skill remains portable without the original `api/` directory. Class entries contain declaration headers, not exhaustive lists of inherited members.

## Lookup by task

| API reference | Look up |
|---|---|
| [Core contracts](api/core.md) | `Screen`, `ParcelableScreen`, `PopResult`, `StaticScreen`, `Presenter`, `Ui`, both factories, `CircuitInject` |
| [Builder and hosts](api/foundation.md) | Factory registration, `CircuitCompositionLocals`, `CircuitContent`, `NavigableCircuitContent`, navigator creation, saver configuration |
| [Navigation](api/navigation.md) | `Navigator`, `StateOptions`, saveable stack creation, active records, answering navigation |
| [Persistence](api/persistence.md) | `CircuitSaver`, typed restoration, serialization registrations, reflective and registered savers |
| [State and effects](api/state.md) | Retention overloads, state producers, Flow collection, impression effects |
| [Overlays](api/overlays.md) | Overlay contracts, host setup, `OverlayEffect`, dialogs, sheet constructors, fullscreen overlay |
| [Android integration](api/android.md) | Android-aware navigator, `IntentScreen`, screen starter, gesture decoration/listeners |
| [Tests](api/testing.md) | `Presenter.test`, `FakeNavigator`, awaiting navigation, `TestEventSink` |

## Coverage gaps

The supplied API download does not include the `circuitx-navigation`, SubCircuit, SubCircuit test, shared-elements, or runtime-navigation module declarations. Their conceptual guidance remains in the task references, but their exact interfaces must be checked in the consuming project's dependencies. In particular, do not invent `SubUiFactory` generics or interceptor signatures from neighboring modules.

Internal packages, processor implementation classes, inherited utility members, and unrelated low-level machinery are intentionally omitted. This is a focused API supplement, not a complete API mirror.

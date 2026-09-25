---
name: slack-circuit-android
description: Implement, refactor, and test Android features using Slack Circuit with class-based presenters, UI implementations, and factories. Use for Circuit screen wiring, state and events, navigation, persistence, overlays, and dependency injection in Android projects.
---

# Slack Circuit for Android

Build Android Circuit features using explicit `Presenter<State>` and `Ui<State>` implementations resolved through `Presenter.Factory` and `Ui.Factory`. Factories may be handwritten or generated for classes/assisted factories.

## Boundaries

- Use classes for Circuit presenters and UIs. Do not register top-level composable functions as presenters or screen UIs, annotate those functions with `@CircuitInject`, or use `presenterOf`/`ui` adapters as a replacement for the classes.
- `@Composable override fun present()` and `@Composable override fun Content()` remain necessary. Ordinary child composables, previews, and composition-root functions are compatible with this architecture.
- Target Android modules, Android lifecycle behavior, and Android tests.
- Preserve the project's DI framework, module boundaries, and navigation approach unless the task calls for changing them. Manual factories are a valid option without DI or code generation.

## Working approach

1. Inspect the version catalog/build files, existing screens, factory bindings, composition root, and tests. Establish the resolved Circuit version, Compose setup, DI mode, and navigation persistence strategy.
2. Read [factories and feature structure](references/factories.md) for screen work. Read [Android setup and DI](references/setup-and-di.md) when changing dependencies or wiring.
3. Model state and events, implement the presenter and UI classes, register their factories, and connect the screen to the appropriate content host. Both factories must resolve the same screen/state pairing and return `null` for unsupported screens.
4. Read only the task-specific references below. Match APIs to the project's resolved dependencies; these references are curated from the supplied documentation snapshot, not a guarantee of availability in every release. Use the [API lookup](references/api-index.md) for bundled declarations and documented coverage gaps. In particular, check `NavStack`, `CircuitSaver`, Parcelable screen interfaces, interception signatures, and experimental SubCircuit support before using them. Inspect dependency sources when the API differs; do not upgrade Circuit just to fit an example.
5. Run the relevant Android compile/KSP tasks and focused tests available in the project. Confirm state/event behavior and factory resolution; check restoration when persistence changes. Report checks actually run and any gaps.

## Task references

- [API lookup](references/api-index.md): selected Android API declarations, packages, overloads, snapshot limits, and missing modules. Load individual API references on demand.

- [State, effects, and presenter recipes](references/state-and-effects.md): retention, Flow collection, async events, loading/error/retry, search, pagination, forms, and composed presenters.
- [Navigation and persistence](references/navigation.md): composition root, screen results, tab stacks, deep links, Android destinations, interception, and saved state.
- [Overlays and UI integration](references/ui-integration.md): dialogs, sheets, shared transitions, gestures, existing Views, and nested components.
- [Testing](references/testing.md): presenter emissions, navigation, UI event sinks, overlays, and restoration.

The references are self-contained and require no access to the original documentation directory.

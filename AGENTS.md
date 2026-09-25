# Agent instructions

These instructions apply throughout this repository. GrowTherapy is an Android app using Kotlin, Jetpack Compose, and Material 3. Inspect the current build files and source before making assumptions about its architecture or dependencies.

## Mandatory skill selection

Skill use is required when a trigger below matches the work. This applies to implementation, debugging, review, refactoring, testing, and technical advice—not just requests that name a skill.

Before doing the relevant work:

1. Match the task to the routing table. Select every applicable skill; one skill does not replace the others.
2. Read each selected `SKILL.md` before applying it. Read its required task-specific references, without loading unrelated references. Reuse instructions already read in this conversation unless they changed.
3. Briefly tell the user which skills you are applying and why.
4. Follow the selected instructions during the work and validation. Reassess skill selection when the scope changes.

If the user explicitly names a skill, load it even when the task wording does not match this table. For tasks outside these triggers, use the available session skill catalog and ordinary repository guidance; do not load unrelated skills solely because they exist.

## Repository skill routing

All paths below are relative to the repository root.

| Skill | Required when                                                                                                                                                                                                                                                                                                                                                                |
| --- |------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [kotlin-coding-standards](.agents/skills/kotlin-coding-standards/SKILL.md) | Writing, changing, reviewing, or explaining Kotlin code, including Kotlin tests. Covers naming, types, nullability, ownership, mutation, error contracts, collections, and API design. Use alongside the more specialized skills below.                                                                                                                                      |
| [kotlin-coroutines-flows](.agents/skills/kotlin-coroutines-flows/SKILL.md) | Work involves suspending operations, coroutine scopes/jobs, cancellation, supervision, dispatchers, shared concurrent state, Flow operators, StateFlow/SharedFlow, retries, sharing, buffering, delivery semantics, lifecycle collection, or coroutine tests.                                                                                                                |
| [android-clean-architecture](.agents/skills/android-clean-architecture/SKILL.md) | Designing or changing layer responsibilities, dependency direction, packages/modules, use cases, repository contracts, data sources, network/database integration, source-of-truth policy, or dependency injection composition. Also required when reviewing these boundaries.                                                                                               |
| [compose-expert](.agents/skills/compose-expert/SKILL.md) | Work involves Compose UI, state, side effects, recomposition, modifiers, layouts, themes, accessibility, motion, navigation, Paging 3, design systems, or translating designs into UI. Also applies to Compose Multiplatform and Android TV work if introduced. Follow its session-start detection and review-mode triggers, and use its routing table to select references. |
| [figma-console-mcp](.agents/skills/slack-circuit-android/SKILL.md) | Route all design-related or design-to-code work using figma-console mcp, following this skill.                                                                                                                                                                                                                                                                               |
| [figma-console-mcp](.agents/skills/figma-console-mcp/SKILL.md) | Route all design-related or design-to-code work using figma-console mcp, following this skill. |
| [slack-circuit-android](.agents/skills/slack-circuit-android/SKILL.md) | Implementing, reviewing, refactoring, or testing Slack Circuit screens, presenters, UIs, state/events, factories, navigation, persistence, overlays, or DI wiring; also when explicitly asked to adopt Circuit. Use its class-based presenter/UI and factory conventions. Its presence alone does not require adding Circuit. |
| [android-cli](.agents/skills/android-cli/SKILL.md) | Using the `android` command, creating/running Android projects through its workflows, managing SDKs/emulators, deploying to devices, inspecting/interacting with device UI, evaluating XML journeys, or searching documentation through the CLI. A source-only edit or ordinary Gradle build does not by itself require a CLI/device workflow. |
| [ide-index-mcp](.agents/skills/ide-index-mcp/SKILL.md) | Load immediately when IDE Index tools such as `ide_index_status`, `ide_find_references`, `ide_find_definition`, `ide_refactor_rename`, or other tools listed by the skill are available. Apply it to semantic code navigation, references, hierarchy, refactoring, diagnostics, and IDE test operations. |
| [jetbrains-debugger](.agents/skills/jetbrains-debugger/SKILL.md) | Load when JetBrains debugger tools listed by the skill are available, or when investigating runtime values, unexplained failures, execution flow, or an explicit debugging request. Use a debugger for uncertain runtime behavior when a usable debug configuration exists; obvious syntax/import fixes do not require a debug session. |
| [yagni-principle](.agents/skills/yagni-principle/SKILL.md) | Evaluating new abstractions, dependencies, extension points, configuration options, framework/module expansion, speculative features, or an extensibility-versus-simplicity tradeoff. Also required for reviews of over-engineering. Preserve quality, tests, and confirmed requirements. |

## Combining skills and resolving overlap

Use the specialist responsible for each concern:

- **Kotlin language and API contracts:** `kotlin-coding-standards`.
- **Async mechanics and async tests:** `kotlin-coroutines-flows`.
- **Layer boundaries and composition:** `android-clean-architecture`.
- **Compose rendering and composition behavior:** `compose-expert`.
- **Design inspection and design-to-code:** `figma-console-mcp`.
- **Circuit contracts and feature wiring:** `slack-circuit-android`.
- **Scope and justified complexity:** `yagni-principle`.
- **Code intelligence, runtime inspection, and device workflows:** `ide-index-mcp`, `jetbrains-debugger`, and `android-cli`, respectively.

For example:

| Task | Required combination |
| --- | --- |
| Change a Compose layout or theme | Kotlin + Compose |
| Translate a Figma design into a Circuit screen | Kotlin + Circuit + Compose + Figma Console |
| Collect a Flow or launch work from a composable | Kotlin + Compose + coroutines |
| Implement repository refresh and caching | Kotlin + architecture + coroutines; add YAGNI when choosing abstractions |
| Build a Circuit feature with async loading and DI | Kotlin + Circuit + Compose + coroutines + architecture |
| Investigate incorrect runtime screen state | Debugger + the applicable Kotlin, Compose, Circuit, and coroutine skills; add Android CLI for device interaction |
| Rename a Kotlin API across callers | Kotlin + IDE Index when available |

Apply higher-priority instructions and explicit user requirements first. This file defines repository routing; the linked skills supply detailed practices. When skills overlap, follow the concern ownership above and preserve compatible requirements from both. Do not migrate frameworks, add libraries, or create layers merely to match a skill example. In particular, a justified repository boundary or Circuit factory is not automatically speculative just because it has one implementation.

For skills installed outside this repository, use the current session catalog to locate their instructions; do not hardcode machine-specific paths here. Load them when explicitly requested or when their task-specific triggers apply. Design/research skills may complement Compose work, but do not replace this project's Android stack with Expo/React Native or iOS-specific APIs.

## Tool availability and blocked skills

- Verify tool availability before relying on a workflow. Never claim to have loaded an unread skill or performed an unavailable tool action.
- For a missing skill file, search the repository and available skill locations. If it remains unavailable, explain the gap. Continue independent work; ask for input only if the missing guidance is necessary to proceed correctly.
- When IDE Index is available, use its semantic tools and follow its readiness/synchronization rules. A temporarily indexing or stale IDE is not a reason to replace semantic operations with text search. Use `rg` for ordinary file/text searches where semantic resolution is unnecessary.
- When the required tool is unavailable, state the limitation and use suitable available checks, such as focused tests, build diagnostics, logs, or source inspection. Distinguish these from semantic reference resolution, debugger evidence, and observed device behavior.
- Skill selection does not automatically require installing plugins, changing the environment, launching a device, or requesting approval. Perform only the setup needed for the authorized task, following the selected skill and applicable permissions.

## Completion checks

Before finishing, verify that every triggered skill was applied and that any newly encountered concern was routed appropriately. Run checks relevant to the change and report what actually passed, failed, or could not be verified. Documentation-only changes need link/content checks rather than an Android build.

Keep changes scoped to the request and preserve unrelated user work. Mention material limitations or deviations in the final response. Do not claim that this instruction file provides a CI or runtime enforcement mechanism; it makes skill selection mandatory for agents following the repository guidance.

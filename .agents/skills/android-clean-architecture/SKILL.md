---
name: android-clean-architecture
description: Design, implement, review, and refactor Android and Kotlin Multiplatform architecture. Use for layer responsibilities, dependency inversion, package and module boundaries, use cases, repository contracts, data-source coordination, persistence and network integration, and dependency injection composition. Complement kotlin-coding-standards and kotlin-coroutines-flows without redefining their language or asynchronous policies.
---

# Android Clean Architecture

## Scope and working approach

1. Inspect the existing architecture, supported targets, source sets, dependency graph, public contracts, and framework versions before proposing changes.
2. Follow `kotlin-coding-standards` when available for Kotlin conventions, type modeling, mutation and ownership, general error policy, and general testing.
3. Follow `kotlin-coroutines-flows` when available for async mechanics, cancellation, dispatchers, synchronization, stream delivery, lifecycle integration, and coroutine testing.
4. Own architectural decisions: where behavior belongs, what contracts cross boundaries, how implementations are composed, and which data is authoritative.
5. Preserve existing contracts and conventions unless the requested change requires migration. Explain meaningful tradeoffs; avoid unrelated refactoring, new stacks, and unnecessary layers.
6. Treat examples as implementation choices. Do not require a ViewModel, presenter framework, DI framework, database, or network client merely because an example uses it.
7. Verify version-sensitive APIs against official documentation and run relevant project checks. Do not upgrade dependencies solely to match an example.

## Layers and dependency direction

- Keep domain policy and contracts independent of Android SDK, UI frameworks, HTTP clients, database APIs, and DI implementation details. Permit deliberately selected platform-independent libraries such as `kotlinx.coroutines` when contracts expose `Flow`.
- Define repository interfaces at the inward boundary that consumes them, usually domain in this architecture. Implement them in data. Do not claim this module arrangement is the only valid Android architecture.
- Keep transport DTOs, persistence entities, HTTP response types, and database handles inside data. Expose domain-facing models or deliberate public read models through repository contracts.
- Put reusable business policy and orchestration in domain when needed. Keep source selection, caching, synchronization, and infrastructure adaptation in data. Keep UI-specific state reduction and display behavior in presentation.
- Let presentation consume use cases or repository interfaces according to complexity and established conventions. Never give it direct database or HTTP service access.
- Compose implementations at the application or platform entry point. A composition root may depend on concrete implementations to wire them; that does not justify such dependencies in domain or presentation logic.
- Avoid dependency cycles. Keep shared modules cohesive; do not create a generic `core` module that accumulates unrelated utilities, framework dependencies, or base classes.

Illustrative compile-time dependencies:

| Consumer | Permitted project dependencies |
| --- | --- |
| App / composition root | Presentation, domain contracts, data implementations |
| Presentation | Domain contracts/use cases; UI/design system as needed |
| Data | Domain contracts; infrastructure libraries |
| Domain | Deliberately selected domain/shared abstractions; no outer layers |

Treat shared abstractions as subject to the same inward-dependency rule. Do not route an infrastructure dependency through `core` to disguise it.

## Packages, modules, and targets

- Distinguish logical layers from Gradle modules. Start with packages when sufficient; split modules for enforceable boundaries, reuse, team ownership, or demonstrated build needs.
- Organize by feature, layer, or a deliberate combination. Define cross-feature public APIs and keep implementation details private to their owners.
- Avoid forcing every feature into an identical set of empty domain/data/presentation modules.
- Keep Android-only code in Android-compatible modules/source sets. Keep shared KMP contracts and implementations compatible with every declared target.
- Share policy and reusable behavior where useful; keep platform adapters and entry-point wiring on appropriate targets. Use interfaces or `expect`/`actual` only for an actual platform boundary.
- Follow existing convention plugins and version catalogs. Leave detailed Gradle engineering to build-specific guidance; do not embed a universal target/plugin template here.

## Domain models and use cases

- Model domain concepts independently of transport and persistence schemas. Follow coding standards for selecting data classes, value classes, enums, sealed hierarchies, or ordinary classes.
- Expose read-only domain data and honor ownership promises. Do not describe `val`, `List`, or a data class as guaranteeing deep immutability.
- Introduce a use case for identifiable business policy, coordination, reuse, or an established operation boundary. Do not require a forwarding class for every repository method.
- Use action-oriented names and optionally `operator fun invoke` consistently with the project. Choose suspending operations versus streams from the contract.
- Keep business policy testable without network, database, UI, or DI initialization. Avoid infrastructure annotations on domain declarations when enforcing this boundary.

## Repository contracts

- Describe behavior rather than database tables or HTTP endpoints. Define meaningful absence, expected failures, ordering, pagination, freshness, and ownership where relevant.
- Decide whether reads are remote-only, local-only, cache-backed, or offline-first. Identify the source of truth for each exposed dataset.
- For observable local state with explicit refresh, distinguish observing stored data from attempting synchronization. A failed refresh need not erase valid cached data; expose the failure separately according to the contract.
- Define write completion: persisted locally, queued for synchronization, or acknowledged remotely. Do not imply that a local write succeeded on the server.
- Choose one coherent failure convention for connected examples and existing APIs: exception-based, `kotlin.Result`, or named domain outcomes as appropriate. Do not switch conventions between a repository, use case, and consumer.
- Translate documented infrastructure failures at a boundary where callers can act on stable semantics. Preserve diagnostic causes appropriately without requiring domain/UI code to inspect vendor exception types.
- Preserve cancellation and unexpected defects according to the specialist skills. Do not convert every throwable into an ordinary domain failure or empty success.
- Specify stream failure and recovery semantics as well as one-shot failures. Do not silently change `Flow<List<T>>` into an outcome stream.
- Use focused repository interfaces. Avoid universal base repositories or generic CRUD contracts unless the domain actually shares those semantics.

## Data-source coordination and persistence

- Let a repository use a DAO or HTTP service directly when sufficient. Introduce data-source wrappers or interfaces when they encapsulate meaningful behavior or an actual substitution boundary.
- Keep mappers near the relevant data types and narrowly visible. Validate untrusted values; define unknown-enum and missing-field behavior rather than inventing valid business defaults.
- Use separate models where schemas, ownership, or semantics differ. Do not mechanically require duplicate models and mappers for identical representations without a boundary benefit.
- Define snapshot versus delta versus page semantics before writing synchronization code. Upserting a complete server snapshot alone does not remove deleted entries.
- Apply replacement only to the response's authoritative scope. Do not delete an entire table for a single category or page response.
- Use a local transaction for related writes that must become visible together. Fetch and validate remote data before a short write transaction; a database transaction does not make remote and local changes jointly atomic.
- Define ordering/conflict policy for overlapping refreshes and local edits. Avoid allowing an older response to overwrite newer authoritative state unintentionally.
- Account for account/tenant ownership and cache cleanup on identity changes where applicable.
- Use explicit serialization/converters or relational tables for structured values. Do not join arbitrary strings with an unescaped delimiter.
- Treat schema migrations and persistence behavior as part of the data contract. Test affected migrations and transaction behavior.
- Use [repository-example.md](references/repository-example.md) for a local source of truth with explicit full-snapshot refresh.

## Network, database, and DI choices

- Preserve the project's selected stack when suitable. For Android/JVM, support Retrofit or Ktor; for networking shared across KMP targets, use a target-compatible client such as Ktor. Do not put Retrofit into common code targeting iOS.
- Treat Room as an option for Android and supported KMP configurations; SQLDelight is another option. Verify target and feature support rather than assigning each database exclusively to one platform.
- Keep client configuration, converters, engines, authentication, status handling, and sensitive logging policy in the infrastructure/composition boundary.
- Reuse owned clients and databases with appropriate lifetimes. Define who closes resources; do not instantiate clients per request.
- Wire plain domain constructors externally. Use Hilt on Android or Koin/manual composition where suitable. Do not introduce a service locator into domain or presentation behavior.
- Ensure examples provide constructor dependencies and bindings consistently. Do not show `@Binds` for a class that cannot be constructed or provided.
- Use [network-examples.md](references/network-examples.md) for Retrofit and Ktor alternatives using the same transport shape. Consult project versions for exact dependencies and setup.

## Architectural verification

- Verify dependency direction with existing module/build rules and focused review. Check that shared source sets do not import platform-only APIs.
- Test business policy independently and repository behavior with faithful fakes or appropriate integration tests.
- Cover affected source-of-truth behavior, refresh failures with cached data, snapshot deletion scope, transaction rollback, conflicting refreshes, and local-versus-remote write completion.
- Check DTO/entity mapping for invalid and missing values, schema evolution, and ownership where promised.
- Verify DI composition using the project's appropriate compilation or graph checks.
- Delegate async test mechanics to `kotlin-coroutines-flows`; do not duplicate its tutorial here.
- Report the chosen boundaries, observable behavior changes, verification performed, and remaining limitations.

## Official references

- [Android architecture guidance](https://developer.android.com/topic/architecture) and [domain layer](https://developer.android.com/topic/architecture/domain-layer): distinguish Google's optional use-case layer from this skill's inward domain-contract arrangement.
- [Retrofit](https://square.github.io/retrofit/) and [Ktor client](https://ktor.io/docs/client-create-new-application.html).
- [Room for KMP](https://developer.android.com/kotlin/multiplatform/room) and [SQLDelight](https://sqldelight.github.io/sqldelight/).

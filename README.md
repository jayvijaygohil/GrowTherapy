# GrowTherapy Android

A native Android UI conversion of the Grow Therapy iOS application, built using **Jetpack Compose**,
**Material 3**, **Slack Circuit**, and **Hilt**.

This project showcases an AI-assisted Android engineering workflow demonstrating how modern AI
coding agents, combined with local Model Context Protocol (MCP) servers, strict repository
guardrails, and official Android developer tooling, can translate visual designs into close to
production-ready Native Android UI.

---

## 💡 Overview & Motivation

The goal of this project is to convert the Grow Therapy iOS application screens (sourced from App
Store screenshots) into a native Android user experience. It highlights day-to-day Senior Android
Engineering practices paired with modern AI capabilities:

1. **Design System & UI Generation**: App Store screenshots were processed
   with [Figma Console MCP](https://github.com/southleft/figma-console-mcp) to construct a tokenized
   design system and UI components using Google's Material 3 library.
2. **AI Agent Code Generation**: An AI coding agent equipped with specialized Android development
   skills in this repo accessed the local Figma Console MCP to translate designs directly into
   idiomatic Jetpack Compose UI—without requiring a paid Figma Dev Tier account.
3. **Strict Architecture & Engineering Guardrails**: Repository rules (`AGENTS.md`) and custom agent
   skills enforce clean architecture, unidirectional data flow (UDF), Kotlin coding standards, and
   YAGNI principles.
4. **Semantic Intelligence & Local Debugging**: Local MCP servers give the AI agent deep IDE
   semantic intelligence, runtime debugging capabilities, and CLI access for fast feedback loops
   without brute-force search overhead.

---

## 🎬 Demo & Design Showcase

### 📱 Application Recordings

#### ☀️ Light Mode

<video src="https://github.com/user-attachments/assets/4673082d-e953-4393-9298-f8f322d94b8d" width="100%" controls></video>

#### 🌙 Dark Mode

<video src="https://github.com/user-attachments/assets/580c343c-4b6d-48ce-8408-e757a831253d" width="100%" controls></video>

---

### 🎨 Figma Design Evolution

#### 1. Reference iOS App Store Screenshots

![1. References](assets/figma_designs/1.%20References.png)

#### 2. Tokenized Material 3 Design System

![2. Design System](assets/figma_designs/2.%20Design%20System.png)

#### 3. Native Android Screen Layouts

![3. Android Screens](assets/figma_designs/3.%20Android%20Screens.png)

---

## 📱 Features & UI Screens

The application includes native implementations for key Grow Therapy user flows:

- 🗓️ **Appointments**: View upcoming therapy sessions, overview details, and status notices.
- 🔍 **Booking**: Care options, therapist discovery, and appointment booking flows.
- 💬 **Messages**: Direct messaging interface, chat bubble threads, and message composer.
- 🧠 **Coach**: Interactive Grow Therapy Coach interface.
- 📊 **Recap**: Post-session summaries, key moments, and session insights.
- 📚 **Resources**: Library of mental health resources, search bar, and featured content cards.
- 🧭 **Navigation Shell**: Unidirectional bottom navigation connecting all primary features via Slack
  Circuit.

---

## 🛠️ AI Tooling & Local MCP Infrastructure

While developing the app, I used local Model Context Protocol (MCP) servers and Google developer
tooling to power a high-precision AI agent development workflow:

| Tool                                                                                        | Purpose & Integration                                                                                                                                                      |
|:--------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **[Figma Console MCP](https://github.com/southleft/figma-console-mcp)**                     | Runs locally to give AI agents access to inspect Figma designs and generate Jetpack Compose layouts without requiring a paid Figma Dev Tier account.                       |
| **[IDE Index MCP Server](https://plugins.jetbrains.com/plugin/29174-ide-index-mcp-server)** | Gives the AI agent IDE-grade semantic indexing, symbol navigation, diagnostics, and project-wide reference tracking instead of relying on slow text/regex searches.        |
| **[Debugger MCP Server](https://plugins.jetbrains.com/plugin/29233-debugger-mcp-server)**   | Enables the agent to inspect runtime call stacks, variable states, and execution flow directly inside Android Studio.                                                      |
| **[Android CLI](https://developer.android.com/tools/agents/android-cli)**                   | Official Google CLI tool allowing the agent to launch emulators, deploy builds, inspect on-device UI hierarchies, evaluate XML journeys, and search Android documentation. |

### 📋 Note on Spec-Driven Development

Since the task was focused specifically on translating visual UI components without building full
domain and data layers, introducing a spec-driven development system upfront would have added
unnecessary overhead for a UI-only scope.

However, when building full end-to-end features (incorporating business logic, network contracts,
and persistence), a spec-driven methodology is the preferred approach:

- **[Spec Kit](https://github.com/github/spec-kit)**: Ideal for greenfield feature development to
  establish formal specifications, requirements, and API contracts before code generation.
- **[OpenSpec](https://openspec.dev/)**: Ideal for brownfield projects to iteratively define,
  validate, and evolve spec contracts within established codebases.

---

## 🏗️ Architecture & Code Guardrails

The app follows modern Android development practices and strict architectural guardrails:

- **Unidirectional Data Flow (UDF)**: Powered
  by [Slack Circuit](https://github.com/slackhq/circuit), decoupling Presenters (`Presenter<State>`)
  from Composable UI implementations (`CircuitUi`).
- **Dependency Injection**: Google [Hilt](https://dagger.dev/hilt/) with KSP code generation for
  Circuit presenter and UI factory bindings.
- **Design System**: Centralized design tokens in `com.jayvijay.growtherapy.ui.theme` (`GrowTheme`,
  `GrowTokens`, `GrowColors`, `GrowTypes`) with reusable atomic controls and cards.
- **Agent Guardrails**: Configured via `AGENTS.md` and `.agents/skills/` covering:
    - `kotlin-coding-standards`: Idiomatic Kotlin, null safety, immutability, and API safety.
    - `slack-circuit-android`: Class-based presenters, UIs, and factory conventions.
    - `compose-expert`: Recomposition efficiency, modifiers, and Material 3 motion.
    - `android-clean-architecture`: Clean separation of layer responsibilities.
    - `yagni-principle`: Simplicity and avoiding speculative over-engineering.

---

## 📦 Tech Stack & Dependencies

All dependencies are centrally managed in `gradle/libs.versions.toml`:

- **Language & Runtime**: [Kotlin 2.4.20](https://kotlinlang.org/)
- **UI Framework
  **: [Jetpack Compose 1.13.0-alpha03](https://developer.android.com/jetpack/compose) & [Material 3 1.5.0-alpha29](https://developer.android.com/jetpack/compose/designsystems/material3)
- **Architecture**: [Slack Circuit 0.39.0](https://github.com/slackhq/circuit) (Foundation, Codegen,
  CircuitX extensions)
- **Dependency Injection
  **: [Hilt 2.60.1](https://dagger.dev/hilt/) + [KSP 2.3.12](https://kotlinlang.org/docs/ksp-overview.html)
- **Static Analysis**: [Detekt 1.23.8](https://detekt.dev/) with [
  `compose-rules-detekt 0.6.7`](https://mrmrk.io/fast/compose-rules/)
- **Testing Dependencies (Configured)**: JUnit4, Circuit Test, Turbine, KotlinX Coroutines Test,
  Robolectric, MockWebServer, Espresso

---

## 🧪 Testing Strategy

Although this repository focuses on UI, for complete feature development, the recommended testing
approach I'd take:

- **Unit Testing**: **JUnit** + **Mockito/MockK** + **MockWebServer** for repository and data source
  testing, combined with **Turbine** and **Circuit Test** for deterministic validation of Presenter
  state flows (`StateFlow` / UDF events).
- **UI & Screenshot Testing**: **Compose UI Test** + **Espresso** for component layout and
  interaction checks, paired with screenshot testing tools (such as Roborazzi or Paparazzi) to catch
  visual regressions against design tokens.
- **End-to-End (E2E) Testing**: **UI Automator** or **Maestro** (or both)—leveraging Maestro for
  fast, declarative YAML-based screen flow automation and UI Automator for system-level dialogs and
  cross-app interactions.

---

## 📂 Project Structure

```
GrowTherapy/
├── .agents/skills/              # Custom AI Agent Skills (Kotlin, Compose, Circuit, MCP)
├── AGENTS.md                    # Mandatory Agent Instructions & Skill Routing
├── assets/                      # Media & Showcase Assets
│   ├── figma_designs/           # Figma design exports (References, System, Screens)
│   └── video/                   # Screen recording app flow video
├── config/detekt/               # Static code analysis configuration
├── gradle/libs.versions.toml    # Centralized Version Catalog
└── app/src/main/java/com/jayvijay/growtherapy/
    ├── di/                      # Dependency Injection (Hilt CircuitModule)
    ├── feature/                 # Feature Modules (Slack Circuit Presenters & UIs)
    │   ├── appointments/        # Appointments flow
    │   ├── booking/             # Booking & therapist discovery flow
    │   ├── coach/               # Grow Therapy Coach flow
    │   ├── messages/            # Direct messaging flow
    │   ├── navigation/          # Navigation Shell & Destinations
    │   ├── recap/               # Session summary & key moments
    │   └── resources/           # Mental health resource library
    └── ui/                      # Design System & UI Components
        ├── atoms/               # Low-level UI atoms (Text, Icon, Image, Divider)
        ├── cards/               # Reusable card & list items
        ├── controls/            # Buttons, Segment controls, Verification badges
        ├── inputs/              # Message & search composer inputs
        └── theme/               # GrowTheme, GrowColors, GrowTokens, GrowTypes
```

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Ladybug (2024.2.1) or newer
- **JDK**: Version 11 or higher
- **Android SDK**: Target SDK 37 (Min SDK 26)

### Building the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/jayvijay/GrowTherapy.html
   cd GrowTherapy
   ```

2. Build the project using Gradle Wrapper:
   ```bash
   ./gradlew assembleDebug
   ```

3. Run static code analysis:
   ```bash
   ./gradlew detekt
   ```

---

## 🔗 Key Links & References

- [Android CLI](https://developer.android.com/tools/agents/android-cli)
- [Figma Console MCP](https://github.com/southleft/figma-console-mcp)
- [IDE Index MCP Server](https://plugins.jetbrains.com/plugin/29174-ide-index-mcp-server)
- [Debugger MCP Server](https://plugins.jetbrains.com/plugin/29233-debugger-mcp-server)
- [OpenSpec](https://openspec.dev/)
- [Spec Kit](https://github.com/github/spec-kit)
- [Slack Circuit Architecture](https://slackhq.github.io/circuit/)

---
name: android-cli
description: Use the Android CLI to create or run Android projects, manage SDKs and emulators, inspect and interact with device UIs, evaluate XML journeys, and search Android documentation. Apply when the task uses the android command or needs its device and development workflows.
---

# Android CLI

Use the installed `android` CLI for the requested Android workflow. Preserve the project's build configuration and the user's chosen device and SDK.

## Check the environment

Run `command -v android` and `android --version`. Use `android <command> --help` (and subcommand help) for the installed syntax. This skill was checked against CLI `1.0.16406183`; help output takes precedence over older examples.

If the CLI is missing, use the installation instructions for the host OS and architecture in the [official Android CLI guide](https://developer.android.com/tools/agents/android-cli). Install it when needed for the authorized task. Do not run `android init`, update the CLI/SDK, or install extra skills as a routine prerequisite: these change the environment and may be unnecessary. Preserve project-local skill placement when managing skills for this project.

Use `android info` for environment diagnosis and `adb devices -l` to identify connected devices. Keep `android --device` options and `adb -s` commands pointed at the same selected serial. If multiple devices are available and the target cannot be inferred, resolve that choice before device interaction.

## Choose the workflow

| Task | Starting commands |
| --- | --- |
| Create a project | `android create --list`, then `android create <template> --name="My App" --output=./my-app` |
| Locate project artifacts | `android describe --help`; inspect its project/build metadata |
| Deploy and launch APKs | `android run --device=<serial> --apks=<apk-paths>` |
| Install without launching | `android install --device=<serial> --apks=<apk-paths>` |
| Manage emulators | `android emulator list`; read subcommand help before create/start/stop/remove |
| Manage SDK packages | `android sdk list --all`; use subcommand help for required package/version syntax |
| Search documentation | `android docs search --help`, then search focused keywords and fetch relevant returned articles |
| Manage Android skills | `android skills --help`; confirm the intended installation scope before adding a skill |

For existing projects, use their build tasks and selected variant; do not create a replacement project. Confirm APK paths rather than guessing a build output. Avoid blanket SDK updates when one missing package is the issue.

## Device interaction and journeys

Read [references/interact.md](references/interact.md) before inspecting or interacting with a device. It covers hierarchy output, screenshots, coordinate selection, and text entry.

For XML journey evaluation, also read [references/journeys.md](references/journeys.md). Follow the specified actions and distinguish application failures from blocked evaluation.

Use Android documentation search for API questions requiring source evidence. If unavailable, consult official Android documentation directly. There is no need to search docs for every routine CLI invocation when installed help answers the question.

## Report results

Separate build success, installation/launch success, and observed UI behavior. A successful command exit alone does not prove a journey passed. Report the selected device, relevant artifacts, and any unverified behavior. Do not retry failed mutating actions blindly; inspect the result first, especially when the action may already have taken effect.

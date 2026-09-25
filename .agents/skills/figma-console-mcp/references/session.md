# Session, targeting, images, and diagnostics

## Target the right file

| Tool | When to use | Arguments and result |
|---|---|---|
| `figma_get_status` | Identify the current file and check connection health | No arguments; returns connection/monitoring status and current URL/context. |
| `figma_list_open_files` | Discover connected files before switching or targeting several files | Schema not documented. Supplies connected file keys and `targetLocked` state. |
| `figma_navigate` | Switch the active file or pin it before edits | `{url, lock?}`. `lock: true` pins the Local target; `false` releases it. Local navigation selects an already-connected file and does not open a new file. |
| `figma_get_selection` | Resolve “this component” or “the selected frame” | Schema not documented; inspect the connected tool definition. |
| `figma_get_design_changes` | Observe design changes during an iterative session | Schema not documented; inspect supported scope and time filters. |
| `figma_diagnose` | Investigate a failed operation after status is insufficient | Schema not documented; use for diagnostics, not routine design inspection. |
| `figma_reconnect` | Recover an existing Local connection when disconnected | Schema not documented. Check status afterwards rather than repeatedly retrying writes. |

A target lock releases if the target disconnects, changes files, or another file is explicitly selected. Confirm the target again after such events. For a one-off Local call into another connected file, `figma_execute({fileKey, code})` avoids changing the active target. For several explicitly identified files, see [canvas editing](canvas.md).

## Choose the right visual read

| Tool | Best use | Arguments and limits |
|---|---|---|
| `figma_get_component_for_development` | Specs plus an image for implementation | `{nodeId, fileUrl?, includeImage?}`; image included by default at 2×. |
| `figma_get_component_image` | Only a rendered node image or export | `{nodeId, fileUrl?, scale?, format?}`; scale 0.01–4, default 2; `png`, `jpg`, `svg`, `pdf`. Returns image URL and metadata. |
| `figma_take_screenshot` | Inspect the editor/plugin UI or supported node screenshot | Tool reference documents `target: "plugin" | "full-page" | "viewport"`, `format: "png" | "jpeg"`, `quality` 0–100 (JPEG), `filename`. Other supplied examples use `nodeId`; verify the actual schema before choosing arguments. |
| `figma_capture_screenshot` | Additional screenshot capability, when exposed | Schema and exact capture behavior not documented; inspect the tool description before use. |

Render the actual affected node after visual edits. If a screenshot is blank or refers to a missing node, confirm file context and rediscover the node ID; do not treat a blank image as verification. Inspect the returned image, not merely a successful tool status. Component image URLs are documented as temporary (30 days); save an asset if the task requires a durable artifact.

## Console tools

| Tool | Choose it for | Arguments |
|---|---|---|
| `figma_get_console_logs` | Existing errors or logs since an operation | `count` default 100; `level`: `log`, `info`, `warn`, `error`, `debug`, `all`; `since`: Unix milliseconds. |
| `figma_watch_console` | Logs while reproducing a problem | `duration`: seconds, default 30, max 300; `level` filter. Use a bounded watch appropriate to the reproduction. |
| `figma_clear_console` | Intentionally clear the captured log buffer | No arguments; returns cleared count. Collect needed evidence before clearing. |
| `figma_reload_plugin` | Explicitly reload the current page | `clearConsole` default true; interrupts the current page state. Not a substitute for reading fresh design data. |

Local console monitoring is documented as capturing Desktop Bridge output, not arbitrary separate plugins. Cloud logs are not the paired plugin's sandbox logs. Do not promise other-plugin debugging from these tools.

Inspect structured `error`, `message`, and `hint` fields. A node-not-found error usually requires rechecking the file and ID; a schema error requires correcting arguments. Stop replaying a failed mutation when its outcome is uncertain and inspect the current state first.

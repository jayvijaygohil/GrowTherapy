# Extract a design system from code

These Local-only tools turn an existing product codebase into a reusable design-system package. Use them when the task starts from production code; for extracting an existing Figma design system, use `figma_get_design_system_kit` instead.

Use absolute `targets` and `outDir` paths. The MCP server's working directory may differ from the project. These tools can write workspace files and progress manifests, including during analysis; choose output paths within the task's authorized scope.

## Tools and sequence

| Tool | When to use | Arguments / result |
|---|---|---|
| `figma_ds_analyze` | First inventory one or multiple apps | Required `targets` array; optional `outDir` (default first target/design-system), `include`/`exclude` substring filters, `maxFiles` default 5000 per target. Returns frameworks, styling/vendor layers, component classifications, usage rank, duplicates, specialization/missing-primitive summary, and full manifest path. |
| `figma_ds_extract_tokens` | After analysis, mine declared and repeated styling values | `outDir?`, `targets?` override, `formats` default dtcg/css-vars, `dtcgDialect` legacy/2025, `minFrequency` default 4, `write` default true (`false` returns inline preview). Returns counts, provenance, warnings, below-threshold samples, output paths. |
| `figma_ds_scaffold` | Create a design-system package from analysis and extracted tokens | Required `outDir`; optional `packageName`, `framework`, `formats`, `dtcgDialect`, `force` default false. Existing scaffold files are skipped by default; token files still refresh. |
| `figma_ds_setup_storybook` | Connect an already-initialized workshop to extracted tokens/styles/fonts | Required `outDir` containing a fresh `.storybook/`. Returns generated/patched files and manual follow-ups. This is a product-output tool, not MCP setup. |
| `figma_ds_extract_component` | Get one component's porting inputs | `outDir`, `component` name from inventory. Returns source (up to 64KB), local imports, prop contract, observed variants, style references, story scaffold, and checklist. |
| `figma_ds_status` | Resume work or record porting progress | `outDir`; optional `update:{component,status,notes?,storyFile?}`. Status: pending, in-progress, ported, skipped. |
| `figma_ds_verify` | Check the extracted package before handoff or token import | `outDir`; returns `passed`, per-check pass/fail/warn/skip details, and Figma round-trip readiness. |

Typical sequence: analyze → extract tokens → scaffold → wire an existing workshop if requested → extract/port components → record status → verify. The component tool supplies a porting manifest; it does not complete the code port for you. The scaffold does not create an initialized Storybook workshop; use `figma_ds_setup_storybook` only when its input already exists.

Analysis helps distinguish bespoke components from vendor wrappers and map usage-specific components to reusable primitives. Do not mechanically port every vendor component. Token extraction distinguishes declared values from frequency-inferred values; review provenance and naming before assigning semantic meaning. Canonical DTCG is always written when writing token outputs.

Verification checks DTCG/alias integrity, invalid quoted CSS expressions, missing CSS variable definitions, story/index structure, and recorded porting coverage. It is not visual verification; render representative stories when the task requires fidelity. After workshop changes, a dev-server restart may be needed to see imported CSS updates.

Progress is available across sessions under the chosen output directory. Read `figma_ds_status` rather than restarting extraction. Importing extracted tokens into Figma is a separate choice: use `figma_import_tokens` only if the requested direction includes code-to-Figma synchronization, and inspect its dry-run plan first.

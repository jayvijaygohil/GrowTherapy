# Read designs and hand off

## Choose the scope

| Tool | Use when | Important arguments / output |
|---|---|---|
| `figma_get_design_system_summary` | Need a quick overview of the current system | No arguments. Component categories/counts, collections, style summary, page overview. |
| `figma_get_design_system_kit` | Need tokens, components, and styles together | `fileKey?`; `include` defaults to `["tokens","components","styles"]`; `componentIds?`; `includeImages` default false; `format`: `full` (default), `summary`, `compact`. |
| `figma_get_file_data` | Locate pages/nodes or inspect a scoped tree | `fileUrl?`, `nodeIds?`, `depth` 0–3 (default 1), `verbosity`: `summary`, `standard`, `full`; `enrich?` for metrics. |
| `figma_get_file_for_plugin` | Need plugin data and component relationships | `fileUrl?`, `nodeIds?`, `depth` default 2, max 5. Includes plugin/shared-plugin data and lightweight bounds; excludes visual styling. |
| `figma_get_component` | Need metadata or a reconstructable specification | `nodeId` required; `fileUrl?`; `format`: `metadata` (default) or `reconstruction`; `enrich` adds metadata quality/token metrics. |
| `figma_get_component_for_development` | Implement a particular UI component | `nodeId`, `fileUrl?`, `includeImage` default true. Layout, typography, visuals, properties, and rendered reference. |
| `figma_get_component_for_development_deep` | Nested components require a complete tree | Unlimited-depth component tree with resolved token names and instance references. Schema not documented; inspect live definition. |
| `figma_analyze_component_set` | Understand variant behavior and differences | State-machine analysis, CSS pseudo-class mappings, cross-variant diffs. Schema not documented. |
| `figma_get_styles` | Need traditional color/text/effect/grid styles specifically | `fileUrl?`, `enrich?`, `export_formats?`, `include_usage?`, `include_exports?`. Returns styles, metadata, requested exports and usage. |

For broad extraction, prefer the kit over separately requesting every family. For a single component, use the development tool. For one color or mode value, use the targeted variable reads in [tokens](tokens.md).

Kit `compact` gives names, types, and property definitions; `summary` omits variant visual details; `full` gives implementation specs. Large responses may be compressed even when requesting `full`; narrow `componentIds` for missing details. The default component inventory covers published components, so do not equate it with all unpublished work. Inspect section-level `errors` before claiming a complete extraction.

Example targeted implementation read:

```javascript
figma_get_component_for_development({
  fileUrl: "https://www.figma.com/design/FILE/Design",
  nodeId: "12:34",
  includeImage: true
})
```

Read relevant annotations with `figma_get_annotations` before implementing behavior that is not visible in the image. Preserve real token and component references in code. Use [review](review.md) for design-code parity and generated handoff docs.

## Reconstruction export

Choose `figma_get_component({nodeId, format: "reconstruction"})` for migration or saving a node-tree specification. It is an export, not an import or automatic recreation operation.

- A normal node returns the raw specification at the root. Component sets may return `{spec, availableVariants, note}`. Inspect the shape rather than assuming every result has `.spec`.
- The spec includes node name/type, geometry, visibility, constraints, fills/strokes/effects, corner radii, auto-layout/padding/gaps/alignment, text/fonts, and children.
- Colors use normalized 0–1 RGB(A), not 0–255 channels or hex strings.
- Supported categories include layout containers, components/sets/instances, shapes/vectors, text, boolean operations, and slices.
- Preserve required fonts and referenced components in the destination. Image references may need re-uploading; complex vectors may lose detail; custom plugin data is excluded.
- `enrich` applies to metadata, not reconstruction. Do not present a reconstruction as a lossless file backup.

For migration via this MCP, use supported creation tools or carefully scoped `figma_execute` code and verify the recreated nodes. The documented compatibility with a separate Component Reconstructor plugin does not imply that plugin is available.

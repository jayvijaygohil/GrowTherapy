# Variables, modes, and token synchronization

## Read tokens

| Tool | Best use | Arguments / result |
|---|---|---|
| `figma_get_variables` | Full variable data, dependencies, usage, optional code exports | `fileUrl?`, `includePublished` default true, `enrich` default false, `export_formats?` (e.g. css/tailwind/sass), `include_usage?`, `include_dependencies?`, `refreshCache?`. Returns collections, variables, modes/values, requested enrichment. |
| `figma_get_token_values` | Values organized by collection/mode without a full system extraction | `collectionName?` filter. |
| `figma_browse_tokens` | User wants an interactive token table | Optional MCP App; schema not documented. Auto-detects current file. Use only if exposed and client supports the UI; otherwise use data reads. |

`figma_get_variables` documents branch URLs with `/branch/BRANCH` or `?branch-id=BRANCH`. Verify returned branch/file context. Request `refreshCache:true` when checking recent variable edits. Library-variable discovery/import is covered in [components](components.md).

## Create and edit

| Tool | When to choose | Arguments |
|---|---|---|
| `figma_create_variable_collection` | Add an empty collection and modes | `name`, `initialModeName?`, `additionalModes?`; returns collection/mode IDs. |
| `figma_create_variable` | Add one variable to an existing collection | `name`, `collectionId`, `resolvedType`, `valuesByMode?`, `description?`, `scopes?`. |
| `figma_update_variable` | Change one variable in one mode | `variableId`, `modeId`, `value`. |
| `figma_rename_variable` | Change naming/grouping while preserving identity | `variableId`, `newName`; `/` groups names. |
| `figma_delete_variable` | Remove a specified variable | `variableId`; inspect usage before removing a referenced token. |
| `figma_delete_variable_collection` | Remove a collection and all its variables | `collectionId`; broader than deleting one variable. |
| `figma_add_mode` | Add a theme/brand/breakpoint mode | `collectionId`, `modeName`; use returned mode ID for values. |
| `figma_rename_mode` | Rename an existing mode | `collectionId`, `modeId`, `newName`. |
| `figma_batch_create_variables` | Add several variables to one collection | `collectionId`, `variables` array of 1–100 `{name,resolvedType,description?,valuesByMode?}`. |
| `figma_batch_update_variables` | Update several values or multiple modes | `updates` array of 1–100 `{variableId,modeId,value}`. |
| `figma_setup_design_tokens` | Create a collection, modes, and tokens together | `collectionName`, `modes` (1–4 names), `tokens` (1–100 `{name,resolvedType,description?,values}`). |

Types: `COLOR` uses hex `#RRGGBB` or `#RRGGBBAA`; `FLOAT` a number; `STRING` text; `BOOLEAN` a boolean. Raw Plugin API colors use a different representation. Mode counts also depend on the file's plan limits.

**Mode names versus IDs:** ordinary `valuesByMode` and updates use mode IDs. `figma_setup_design_tokens` uses `values` keyed by mode **names**. Its values can be literals or DTCG brace aliases such as `{color.blue.600}` and collection-qualified references. Forward references within the same call are supported; unresolved aliases produce per-item warnings.

```javascript
figma_setup_design_tokens({
  collectionName: "Theme", modes: ["Light", "Dark"],
  tokens: [
    {name:"color/background",resolvedType:"COLOR",values:{Light:"#FFFFFF",Dark:"#111827"}},
    {name:"color/surface",resolvedType:"COLOR",values:{Light:"{color.background}",Dark:"{color.background}"}}
  ]
})
```

For existing collections, choose batch create/update rather than creating a second collection. Review `created`/`updated`, `failed`, and individual `results`; a successful top-level response can contain failed items. Deletions have no promised programmatic undo.

## Export to code: `figma_export_tokens`

Use for actual token files or an inline multi-format export. Use read tools above for inspection only.

Documented options include `format`, `outputPath`, `splitByMode`, `splitByCollection`, `prefix`, `scope:"collection"`, `collectionIds`, `dtcgDialect`, `strategy`, and `configPath`. Zero arguments can use an existing `tokens.config.json`; inspect that file's target and output scope before relying on it. Verify other options against the connected schema.

| Output format | Choose for |
|---|---|
| `dtcg` | Canonical JSON and reliable round-trip IDs/aliases/metadata |
| `css-vars` | Mode-aware CSS custom properties |
| `tailwind-v4` | `@theme inline` utility namespace mapping |
| `tailwind-v3` | Theme-extension object for a v3 config |
| `scss` | SCSS variables and multi-mode maps |
| `ts-module` | Typed token object |
| `json-flat` | Flat token-name/value mapping |
| `json-nested` | Nested path-based token tree |
| `style-dictionary-v3` | Legacy `{value,type,comment}` source format |
| `tokens-studio` | Multi-file token sets, themes, and metadata |

DTCG `dtcgDialect:"legacy"` (default) emits hex colors and numeric dimensions. `"2025"` emits object colors and `{value,unit:"px"}` dimensions. Import accepts both, including mixed documents.

`outputPath` may be a directory or exact file path. A file path requires exactly one output file; split or multi-format output needs a directory. Exports replace destination contents, not individual tokens. Default `strategy:"merge"` guards against deleting unmanaged tokens from existing DTCG files, missing requested collections, empty exports, and recorded source-file mismatches. `"replace"` bypasses overwrite guards; use only when replacing that destination is intended. `"dry-run"` previews destinations via `wouldWriteTo`.

```javascript
figma_export_tokens({
  scope: "collection", collectionIds: ["COLLECTION_ID"],
  format: "dtcg", outputPath: "/project/tokens/colors.tokens.json",
  strategy: "dry-run"
})
```

Check returned `source.fileKey`/`source.fileName`. Older token files and some plain-JSON formats lack source-file protection. DTCG preserves variable/collection IDs, sync snapshots, scopes, and code syntax in `$extensions["figma-console-mcp"]`; keep this metadata for future imports. External library aliases may appear as `{__library:VariableID:...}`; CSS outputs may skip these with a comment. Do not silently replace unresolved references with guessed colors.

Cloud exports return content inline: omit local `configPath`/`outputPath`, then save returned content through workspace file tools if requested.

## Import from code: `figma_import_tokens`

Use to apply code-side token changes to Figma. Pass `format` and inline string `payload` (single file), or `files` for multi-file input; inspect the live schema for the `files` structure. Local calls may use an existing config. Cloud calls use inline content and omit `configPath`.

| Strategy | Effect |
|---|---|
| `dry-run` | Compute a diff without changing Figma; useful before reviewing bulk changes. |
| `merge` (default) | Apply changed values/creates/renames/aliases; preserve Figma-only variables. |
| `replace` | Apply replacement semantics, including deletion of Figma-only variables. |

Tokens match first by stored Figma variable ID, then by token path; unchanged values avoid writes. Preserved IDs allow renames without duplicate creation. Default `onConflict:"ask"` reports two-sided conflicts and writes nothing. `figma-wins`, `code-wins`, or `skip` implement an already-decided conflict policy; do not silently select a winner.

Read the plan's creates, updates, and deletes before applying. Literal and alias tokens are supported, but unresolved references and unsupported TIMING/EASING types can be skipped with warnings. Inspect `applyResult.errors[]` and reread changed values after import. A partial import is not an all-or-nothing transaction.

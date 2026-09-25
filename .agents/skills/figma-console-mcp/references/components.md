# Components, libraries, properties, and slots

## Discovery and reuse

| Tool | When to choose it | Arguments / result |
|---|---|---|
| `figma_search_components` | Search by name/description before creating a duplicate | `query?`, `category?`, `libraryFileKey?` or `libraryFileUrl?`, `limit` default 10/max 25, `offset?`. Returns keys, variants, and `source` local/library. |
| `figma_get_library_components` | Browse a published library's inventory | `libraryFileKey` or `libraryFileUrl`, `query?`, `includeVariants` default false, `limit` default 25/max 100, `offset?`. Local only. |
| `figma_get_library_component_by_key` | Have a component/set key but no library URL; need properties or variant keys | `componentKey` required; `includeVisualSpecs` default true; `format`: `full` or `summary`. Returns source file/node, properties, variants with individual keys, visual specs, warnings. |
| `figma_get_component_details` | Inspect a known component by key | `{componentKey}`; properties, variants, metadata. |
| `figma_instantiate_component` | Place an existing local or library component | `{componentKey, x?, y?, overrides?}`; returns the instance node ID. Use exact discovered property names in `overrides`. |

Resolve a set key to its variant keys before instantiation. Instantiate with an individual **variant key**, not the component-set key. A node ID such as `12:34` is not a published key. Key-resolution results can strip visual specs on large responses; inspect `compression` and `warnings`.

```javascript
figma_search_components({query: "Button", libraryFileKey: "LIBRARY"})
// Resolve a returned key, then use a chosen variants[].key:
figma_get_library_component_by_key({componentKey: "KEY_FROM_SEARCH"})
figma_instantiate_component({
  componentKey: "VARIANT_KEY_FROM_RESULT",
  x: 100, y: 200,
  overrides: {"Button Label": "Continue", "Show Icon": false}
})
```

## Shared library variables

| Tool | When to use | Arguments / result |
|---|---|---|
| `figma_get_library_variables` | Discover tokens available from subscribed libraries | Optional `libraryName`, `collectionName` substring filters; `resolvedType`: `COLOR`, `FLOAT`, `STRING`, `BOOLEAN`. Returns collections and variable `key`, `name`, `resolvedType`. |
| `figma_import_library_variable` | Make a selected library token locally addressable for bindings | `{variableKey}`; returns `imported.id`, key, type, collection, metadata. Repeating the same import returns the same local ID. |

Only libraries enabled in the current file are listed. Importing is not subscribing to a new library. Use the returned local variable ID for bindings; do not substitute the published variable key. Import does not grant permission to edit the source library token.

## Component sets and arrangement

`figma_create_component_set` is the dedicated choice for variant matrices or combining loose components.

| Mode | Arguments | Behavior |
|---|---|---|
| Generate from a base | `baseComponentId` plus `properties`, e.g. `{State:["default","hover"],Size:["sm","lg"]}` | Clones each combination; the original base becomes the first variant and retains its ID. Clones initially share the base's appearance; apply state/size-specific visual changes afterwards. |
| Combine existing | `componentIds`, optional aligned `variantProperties` maps | Combines COMPONENT nodes; rejects components already inside a set. Existing names are used unless overridden. |

The two modes are mutually exclusive. Shared options: `name`, `parentId`, `position: {x,y}`, `autoArrange` (default false), `arrangeOptions: {gap,cellPadding,columnProperty}`. Variant property names/values cannot contain `=` or `,`. Maximum 100 combinations; above about 40, consider smaller sets when compatible with the desired component API. Returns set ID/key and each variant's node ID/key.

`figma_arrange_component_set` organizes an existing set with labels and a container. Use `componentSetId` or `componentSetName` (otherwise selection) and `options: {gap, cellPadding, columnProperty}`; defaults are gap 24 and cell padding 20. This edits layout; it is not a read-only preview.

## Component property definitions

| Tool | Purpose | Arguments |
|---|---|---|
| `figma_add_component_property` | Add a component API property | `nodeId`, `propertyName`, `propertyType` (`BOOLEAN`, `TEXT`, `INSTANCE_SWAP`, `VARIANT`), `defaultValue`. |
| `figma_edit_component_property` | Rename/change a property's default | `nodeId`, `propertyName`, `newValue: {name?, defaultValue?}`; check live schema for additional fields. |
| `figma_delete_component_property` | Remove a specific property definition | `nodeId`, `propertyName`. |
| `figma_set_instance_properties` | Change existing instance overrides, when exposed | Mentioned in the supplied docs but schema not documented. Cannot populate slots. |
| `figma_set_description` | Document a component, set, or style | `nodeId`, `description` (markdown supported). |

Read definitions before editing and retain exact property keys, including suffixes when returned. Variant-axis definitions belong to the set, not individual variant nodes. Property definitions and instance values are different operations.

## Slots

| Tool | Choose it for | Arguments and constraints |
|---|---|---|
| `figma_create_slot` | New slot inside a COMPONENT | `nodeId`, `name?`, independent `width?`/`height?`, `layoutMode?`: `NONE`, `HORIZONTAL`, `VERTICAL`. Creates its linked SLOT property automatically; GRID is rejected. |
| `figma_get_slots` | Discover slot IDs/names and current contents | Reads component, component set, or instance; set results identify each variant. Exact argument schema not documented. |
| `figma_append_to_slot` | Populate an instance's slot | `instanceId` + `slotName`, or `slotId`; use `sourceNodeId` to clone existing content (`clone:false` moves it), or `nodeType` + `properties` to create content. `clearExisting:true` replaces existing content after validation. |
| `figma_reset_slot` | Empty an instance slot | `slotId` or `instanceId` + `slotName`. |
| `figma_add_slot_property` | Retrofit an existing frame as a slot | Supports `description` and `preferredValues`; full schema not documented. Prefer `figma_create_slot` for new content slots. |

For a component set, create a slot on each variant COMPONENT, not on the set. Slot content cannot be assigned via instance-property overrides. Appending a main component is rejected; use an instance. New slot content types: `FRAME`, `RECTANGLE`, `ELLIPSE`, `TEXT`, `LINE`, `POLYGON`, `STAR`, `VECTOR`.

```javascript
figma_append_to_slot({
  instanceId: "12:90", slotName: "Content",
  nodeType: "TEXT", properties: {text: "Details", name: "Body", width: 200, height: 24}
})
```

# Canvas editing and custom operations

## Dedicated node tools

Use these for a clear single-purpose edit after reading the node and confirming the target file.

| Tool | Use | Documented arguments |
|---|---|---|
| `figma_resize_node` | Change dimensions | `nodeId`, `width`, `height` |
| `figma_move_node` | Change position | `nodeId`, `x`, `y` |
| `figma_clone_node` | Duplicate an existing node | `nodeId`; returns the new ID |
| `figma_delete_node` | Remove a requested node and its contained design | `nodeId`; no programmatic undo promised |
| `figma_rename_node` | Change a layer name | `nodeId`, `newName` |
| `figma_set_text` | Replace text-node content | `nodeId`, `characters` |
| `figma_set_fills` | Set node fills | `nodeId`, `fills`, e.g. `[{type:"SOLID",color:"#FF0000"}]` |
| `figma_set_strokes` | Set outlines | `nodeId`, `strokes`, `strokeWeight`, e.g. solid hex paint |
| `figma_create_child` | Create a child under a known parent | `parentId`, `type`, `name`; documented example uses `type:"FRAME"`; inspect schema for supported types/properties |
| `figma_set_image_fill` | Apply an image to one or more nodes | `nodeIds`, `imageData`, `scaleMode`: `FILL` (default), `FIT`, `CROP`, `TILE` |

Image data is base64 PNG/JPEG, or an absolute image path in Local mode. Cloud calls require inline data. Returns image hash, updated count, and updated nodes; compare count with requested IDs. Setting fills/strokes supplies the desired paint array, so read and preserve any existing paints that must remain.

Hex colors are accepted by documented helper tools; raw Plugin API paints use normalized numeric RGB. Avoid copying helper-tool arguments directly into Plugin API assignments.

## `figma_execute`

Use for custom frame/auto-layout creation, complex geometry, page organization, variable binding, or operations not covered by dedicated tools. Arguments: `code` required; `timeout` default 5000 ms/max 30000; `fileKey` optional and Local-only.

The code runs with the Figma Plugin API `figma` global. MCP tools are not functions inside that code. Await async lookups/font loading, validate node types before modifying them, and return compact IDs/results for follow-up work. A timeout is not proof that earlier mutations rolled back.

Example: create a reusable button with a loaded font, then inspect/render its returned ID.

```javascript
figma_execute({
  code: `
    const font = { family: "Inter", style: "Medium" };
    await figma.loadFontAsync(font);
    const button = figma.createComponent();
    button.name = "Button";
    button.layoutMode = "HORIZONTAL";
    button.primaryAxisSizingMode = "AUTO";
    button.counterAxisSizingMode = "AUTO";
    button.paddingLeft = button.paddingRight = 16;
    button.paddingTop = button.paddingBottom = 12;
    button.cornerRadius = 8;
    button.fills = [{type:"SOLID",color:{r:0.1,g:0.3,b:0.8}}];
    const label = figma.createText();
    label.fontName = font;
    label.characters = "Continue";
    label.fills = [{type:"SOLID",color:{r:1,g:1,b:1}}];
    button.appendChild(label);
    button.x = figma.viewport.center.x;
    button.y = figma.viewport.center.y;
    figma.currentPage.selection = [button];
    return {nodeId:button.id, textId:label.id};
  `,
  timeout: 10000
})
```

Use actual project tokens/styles instead of the illustrative literal paints when available. For existing mixed-font text, load the fonts used by the affected ranges before editing. Prefer `figma_create_component_set` over scripting a variant matrix and token batch tools over one execute call per variable.

## `figma_execute_across_files`

Local-only; runs the same code against multiple connected files. Use for a repeated audit or an explicitly scoped mechanical update.

- Required `code`; specify `fileKeys` or `allFiles:true`.
- `timeout` is per-file, default 10000 ms/max 30000.
- Prefer explicit `fileKeys` for writes. `allFiles:true` includes every connected file, even one protected by the active-target lock.
- Results include a per-file `success`, `result` or `error`, and `fileContext`; plus totals and `missingFileKeys`.
- One failed file does not cancel successful files. Inspect every result; retry only unresolved work after reading its state.

For a single alternate file, use `figma_execute({fileKey,code})`; it does not change the active target or release its lock. Verify returned `fileContext` against the intended file.

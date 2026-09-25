---
name: figma-console-mcp
description: Use an already-connected Figma Console MCP to inspect, create, edit, and validate Figma designs, components, tokens, FigJam boards, and Slides, or translate designs into code and documentation. Provides tool selection and usage references for design work; excludes MCP installation and configuration.
---

# Figma Console MCP

Use figma-console-mcp for design inspection, creation, editing, and verification. The connection is already configured. This skill describes the exposed tools and their use, not server internals or installation.

## Working approach

1. Identify the intended file, editor, and nodes from the user's URL, selection, or connected-file inventory. Use `figma_get_status` when the target or connection is uncertain. For multi-file writes, confirm the target and use `figma_navigate` with `lock: true` where supported.
2. Read only the reference relevant to the task. Start with a summary or scoped query, then inspect the specific components, styles, variables, and annotations needed.
3. Prefer existing library components and tokens. Use dedicated tools for supported operations; use `figma_execute` for custom layouts, compound edits, or Plugin API operations without a dedicated tool.
4. Apply the requested changes. Retain returned IDs and inspect per-item errors in batch results. After an uncertain write or timeout, read the affected objects before retrying to avoid duplicate creation.
5. Verify the result with fresh targeted data and a rendered image for visual changes. Use parity or accessibility tools when those checks match the task. Report actual changes and any incomplete results.

A design request does not by itself authorize posting comments to other people, publishing libraries, or unrelated file mutations. Use those actions only within the user's authorized scope. Do not turn a review request into automatic design fixes.

## Choose a reference

| Task | Read |
|---|---|
| Select a file, inspect selection, capture an image, diagnose an operation, read logs | [Session and visual inspection](references/session.md) |
| Extract design specs, inspect styles, implement UI, export reconstruction data | [Read designs and hand off](references/read-designs.md) |
| Find/import components, instantiate variants, build component sets, edit properties or slots | [Components and libraries](references/components.md) |
| Create layouts, edit nodes, apply images, execute custom Figma operations | [Canvas editing](references/canvas.md) |
| Read/change variables, manage modes, import/export token files | [Variables and token sync](references/tokens.md) |
| Audit design quality, compare design with code, generate docs, read/write annotations or comments, query history | [Review and documentation](references/review.md) |
| Extract a design system from an existing codebase | [Codebase extraction](references/codebase-extraction.md) |
| Read or create FigJam diagrams, boards, tables, or notes | [FigJam](references/figjam.md) |
| Inspect or edit a presentation, slide content, order, or transitions | [Slides](references/slides.md) |

## Tool naming and schema authority

Names here are the unprefixed MCP names; invoke the corresponding tool from the connected figma-console-mcp server, using its actual namespace. Examples show tool arguments, not shell commands or callable JavaScript globals inside `figma_execute`.

These references are curated from the supplied `tools.md`, `figjam.md`, `slides.md`, `reconstruction-format.md`, `mcp-apps.md`, and usage documentation. Tool inventories vary; do not rely on a fixed count. Check the exposed tool schema for required fields, supported enums, and availability. Tools whose schemas are absent from the supplied documentation are explicitly marked **schema not documented**. Do not invent their arguments.

Keep IDs distinct: file keys identify files; node IDs identify layers; component keys identify reusable library assets; variable keys identify library tokens; local variable IDs and mode IDs address values. Use IDs returned from reads or creation results. Parameter spelling is tool-specific (`nodeId`, `node_id`, and `node_ids` are not interchangeable).

If a tool is unavailable, use an available figma-console-mcp operation with equivalent scope, such as `figma_execute` for a supported Plugin API operation. If no equivalent exists, report the capability gap without launching a setup workflow or claiming the operation succeeded.

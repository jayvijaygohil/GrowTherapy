# FigJam boards and diagrams

Use these tools only against a FigJam board. Confirm editor/file context before creation. Read an existing board before adding nodes to avoid duplicating or covering content.

## Read and analyze

| Tool | Use | Arguments / output |
|---|---|---|
| `figjam_get_board_contents` | Summarize a board or locate existing nodes | `nodeTypes?`, `maxNodes` default 500/range 1–1000. Returns nodes with IDs, text, positions, dimensions, type-specific data, current page, `totalFound`, and `truncated`. |
| `figjam_get_connections` | Understand flow direction, branches, and graph structure | No arguments. Returns edges `{connectorId,startNodeId,endNodeId,label}` and connected-node lookup/counts. |

Board filters: `STICKY`, `SHAPE_WITH_TEXT`, `CONNECTOR`, `TABLE`, `CODE_BLOCK`, `SECTION`, `FRAME`, `TEXT`. If `truncated` is true, narrow by type or increase the documented cap; do not describe a partial read as the whole board. Use connection data rather than inferring links solely from spatial proximity.

## Create and arrange

| Tool | When to choose | Arguments / limits |
|---|---|---|
| `figjam_create_sticky` | One note | Required `text` (max 5000 chars); optional `color`, `x`, `y`. |
| `figjam_create_stickies` | Meeting notes, affinity groups, retrospective cards | `stickies:[{text,color?,x?,y?}]`, up to 200 per call. |
| `figjam_create_shape_with_text` | A flowchart step, decision, or labeled diagram object | `text?`, `shapeType?` (default ROUNDED_RECTANGLE), `x?`, `y?`. |
| `figjam_create_connector` | Connect already-created nodes | `startNodeId`, `endNodeId`, optional `label`. |
| `figjam_create_table` | Comparison matrix or structured board data | `rows` 1–100, `columns` 1–50; optional row-major `data:string[][]`, `x`, `y`. |
| `figjam_create_code_block` | Technical snippet on the board | `code` max 50000 chars; optional `language`, `x`, `y`. |
| `figjam_auto_arrange` | Align a selected set of board objects | `nodeIds` max 500; `layout`: grid (default), horizontal, vertical; `spacing` default 40; `columns?` (grid default based on square root of node count). |

Sticky colors: `YELLOW`, `BLUE`, `GREEN`, `PINK`, `ORANGE`, `PURPLE`, `RED`, `LIGHT_GRAY`, `GRAY`.

Shape types: `ROUNDED_RECTANGLE`, `DIAMOND`, `ELLIPSE`, `TRIANGLE_UP`, `TRIANGLE_DOWN`, `PARALLELOGRAM_RIGHT`, `PARALLELOGRAM_LEFT`, `ENG_DATABASE`, `ENG_QUEUE`, `ENG_FILE`, `ENG_FOLDER`.

Code language examples: `JAVASCRIPT`, `PYTHON`, `TYPESCRIPT`, `JSON`, `HTML`, `CSS`; check live enum for other languages.

## Workflow choices

- For an affinity map, batch-create notes, retain their IDs, and arrange only the intended cluster; preserve unrelated board layout.
- For a user flow, create the steps/decision shapes, then connect returned IDs and label branches. Read connections afterwards to confirm endpoints.
- For a board summary, read contents plus connections; no writes are needed.
- For a comparison, use a table instead of manually aligning many text nodes.

Verify created content with a fresh board read and an appropriate available visual capture. Do not use Figma Design-only component tools as replacements for FigJam-native elements.

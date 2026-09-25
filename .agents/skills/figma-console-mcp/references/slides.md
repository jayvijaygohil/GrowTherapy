# Figma Slides

Use these tools against a Figma Slides presentation. The supplied Slides documentation lists tool purposes but not argument schemas. For every tool below, inspect the connected definition for slide identifiers, index conventions, required fields, and supported properties before calling it. Do not guess `slideId`, index, or transition-field names from the tool name.

## Read before editing

| Tool | When to choose it | Expected information |
|---|---|---|
| `figma_list_slides` | Inventory the deck, resolve named slides, inspect presentation order | Slides with order, names, and skip status |
| `figma_get_slide_content` | Review wording, inspect an existing layout, or locate content for edits | Full content of a particular slide, including text/shapes/frames |
| `figma_get_slide_grid` | Understand spatial arrangement before deck restructuring | Two-dimensional grid layout |
| `figma_get_slide_transition` | Read existing transition settings | Style, easing, duration |
| `figma_get_focused_slide` | Resolve “this slide” | The currently focused slide |
| `figma_get_text_styles` | Reuse deck typography | Local text-style IDs, font information, sizes |

`figma_get_text_styles` is a read despite being grouped with write tools in the source documentation. Read actual content before summarizing or choosing a template slide to duplicate.

## Create and edit

| Tool | Use when |
|---|---|
| `figma_create_slide` | Add a new blank slide |
| `figma_duplicate_slide` | Reuse an existing slide's layout, formatting, and content |
| `figma_delete_slide` | Remove a slide specifically within the requested edit scope |
| `figma_reorder_slides` | Change deck sequence; first read current order/grid and use the live schema's ordering convention |
| `figma_skip_slide` | Preserve a slide while excluding it from presentation playback |
| `figma_add_text_to_slide` | Add text with custom font, color, alignment, wrapping, and case |
| `figma_add_shape_to_slide` | Add structural shapes to a slide |
| `figma_set_slide_background` | Set background color; creates or updates a full-slide rectangle |
| `figma_set_slide_transition` | Change transition style, easing, and duration |

Adding text is not the same as replacing an existing text node. Inspect content IDs and use a supported node text edit when revising existing wording. After duplication or addition, use returned IDs rather than assuming old order indexes still identify the same slide.

## Editor navigation

| Tool | Use when |
|---|---|
| `figma_set_slides_view_mode` | Switch between the deck grid and a single-slide view |
| `figma_focus_slide` | Navigate the editor to a specific slide |

Focus changes the editor view; reorder changes the deck. Choose the operation that matches the request.

## Transitions

Documented styles:

| Family | Values |
|---|---|
| Basic | `NONE`, `DISSOLVE`, `SMART_ANIMATE` |
| Slide in | `SLIDE_FROM_LEFT`, `SLIDE_FROM_RIGHT`, `SLIDE_FROM_TOP`, `SLIDE_FROM_BOTTOM` |
| Push | `PUSH_FROM_LEFT`, `PUSH_FROM_RIGHT`, `PUSH_FROM_TOP`, `PUSH_FROM_BOTTOM` |
| Move in | `MOVE_FROM_LEFT`, `MOVE_FROM_RIGHT`, `MOVE_FROM_TOP`, `MOVE_FROM_BOTTOM` |
| Slide out | `SLIDE_OUT_TO_LEFT`, `SLIDE_OUT_TO_RIGHT`, `SLIDE_OUT_TO_TOP`, `SLIDE_OUT_TO_BOTTOM` |
| Move out | `MOVE_OUT_TO_LEFT`, `MOVE_OUT_TO_RIGHT`, `MOVE_OUT_TO_TOP`, `MOVE_OUT_TO_BOTTOM` |

Easing: `LINEAR`, `EASE_IN`, `EASE_OUT`, `EASE_IN_AND_OUT`, `GENTLE`, `QUICK`, `BOUNCY`, `SLOW`. Duration: 0.01–10 seconds. Verify enums against the exposed schema because the documentation's stated count differs from its enumerated styles.

When no motion preference is given, the docs suggest `DISSOLVE` with `EASE_IN_AND_OUT` at 0.5 seconds. Use `SMART_ANIMATE` when consecutive slides share matching elements. Preserve existing motion conventions unless the task calls for a change.

For a deck-wide transition update: list slides → read representative existing settings → apply the requested settings to the intended slides → read transitions back. For restructuring: inspect order/grid → perform scoped duplication/reordering/skipping/deletion → list again. For content edits, read the resulting slide and inspect a rendered view.

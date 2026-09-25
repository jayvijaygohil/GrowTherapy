# Evaluating XML journeys

A journey describes an ordered test of app behavior:

```xml
<journey name="Home navigation">
  <description>Open the Home screen.</description>
  <actions>
    <action>Tap the Home icon</action>
    <action>Verify that the Home screen is visible</action>
  </actions>
</journey>
```

Read [interact.md](interact.md) before evaluation. Use the user-specified app, device, and initial state. Follow the action order literally; do not substitute an inferred intention for a step such as “tap the first search result.” Treat the journey as the expected behavior unless the user has explicitly changed it.

## Actions and assertions

- Interaction steps: perform the stated action, inspect the result, and record crashes or unexpected behavior. Do not invent extra acceptance criteria. Split a compound instruction into its ordered sub-actions while preserving its original text in the report.
- Assertion steps, such as “check” or “verify”: inspect the current state without interacting to make the assertion true. “Check that Switch 2 is visible” fails if it is not currently visible; scrolling would change what is being tested.
- Multiple expectations in one step must all hold for that step to pass. Use screenshots for visual properties such as color that the hierarchy cannot establish.
- If the instruction is malformed or ambiguous enough that it cannot be evaluated, stop and record the issue. Do not reject valid assertions merely because they are not UI interactions.

Stop at the first failed step, unexpected app exit/crash, or confirmed freeze. A requested exit is not an automatic failure. A transient loading state is not proof of a freeze; use a bounded wait and fresh observation as described in the interaction reference.

If a tool error, disconnected device, or missing prerequisite prevents evaluation, mark it **blocked**, not an application failure. One diagnostic retry or alternate observation is reasonable; do not assume failed tools always provide valid app evidence. Mark later steps **not run**.

Keep app fixes separate from the evaluation. Do not change the app, reset its data, or modify the journey to force a pass. If the user also requested a fix, retain the initial failure evidence and report a later rerun separately.

## Results artifact

Write a Markdown report to the requested output location, or a suitable project test-artifact directory. Include:

- Journey name, selected device/app, initial conditions, and overall outcome.
- Each original action in order, with **passed**, **failed**, **blocked**, or **not run** status. “Evaluated” alone is not a pass.
- Commands performed and evidence supporting the outcome, with links to saved layouts or screenshots where useful. Redact secrets from recorded input commands.
- The first failure/blocker and remaining steps not run. Keep observations separate from proposed fixes.

Example step:

```markdown
### 2. Verify that the Home screen is visible — failed

- Observed: The Settings screen remained visible after tapping Home.
- Evidence: [Screenshot](screenshots/step-2.png)
- Remaining steps: Not run.
```

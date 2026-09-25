# Testing Android Circuit features

See [testing APIs](api/testing.md) for test parameters and navigation/event assertion methods.

Use `circuit-test` at the project's Circuit version. For Android local unit tests, the supplied docs use `android.testOptions.unitTests.isReturnDefaultValues = true`; apply it where the test setup requires Android stubs, following existing project conventions.

## Presenters and factory wiring

Instantiate presenter classes with fake repositories and `FakeNavigator`. Run `Presenter.test` inside `runTest` to assert state and dispatch events:

```kotlin
@Test
fun incrementChangesCount() = runTest {
  val presenter = CounterPresenter(FakeNavigator(CounterScreen))
  presenter.test {
    val initial = awaitItem()
    assertEquals(0, initial.count)
    initial.eventSink(CounterEvent.Increment)
    assertEquals(1, awaitItem().count)
  }
}
```

Use controlled repository emissions to test loading, content, failure, retry, and cancellation behavior. `awaitItem()` filters unchanged states; do not wait for a new state after an event that only navigates. Compare meaningful fields rather than whole state objects whose event-sink lambdas may differ. Send subsequent events through the latest received state's sink.

Use `FakeNavigator.awaitNextScreen()` for a navigation event; check the installed fake's pop/reset inspection methods when asserting those operations. Verify the screen arguments/result, not just that some navigation happened.

Factory checks should establish that each expected screen resolves both matching implementations and that unrelated screens return `null`. For generated factories, compile KSP and the consuming module to validate actual DI contributions; an annotation alone is insufficient evidence of registration.

## UI events

Render `FeatureUi().Content(state, Modifier)` with a Compose test rule using fixed state and `TestEventSink<Event>`. Assert visible content, perform interactions, and assert emitted events. Cover mutually exclusive loading, empty, failure, and loaded variants as appropriate. Use the project's existing snapshot framework for appearance checks when needed. Rendering supplied state should not require a repository or presenter.

## Overlays

A presenter test does not need a real overlay host. Assert that the request field becomes non-null, dispatch the same answer event the UI would emit, and verify both confirmation and cancellation/dismissal paths. For asynchronous repository operations, await observable completion or advance the controlled scheduler rather than asserting immediately after launch.

Exercise the actual overlay in UI tests when dismissal behavior or host wiring changes.

## Persistence and navigation integration

When modifying persistence or navigation, verify the actual host and configured saver, not just `FakeNavigator`:

- Round-trip screen and result arguments through the chosen saver.
- Recreate the Activity and verify the active screen and editable state.
- Exercise process-state restoration separately from in-memory retention; rotation alone does not prove process-death behavior.
- Request a result, recreate the host, and complete it; assert delivery to the requesting record only.
- For tabs, navigate within one tab, switch away and back, and verify its stack.
- For deep links, verify cold start, delivery to the running Activity, and the expected Back destination.

Run only checks relevant to the change and state explicitly when device or restoration tests could not be executed.

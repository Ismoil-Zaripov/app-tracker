---
name: compose-screen
description: How every screen is structured in this project (layout, ViewModel, state, previews).
---

## File layout per screen

```
ui/<feature>/
  <Feature>Screen.kt      // @Composable fun, receives state + lambdas, no VM reference
  <Feature>ViewModel.kt   // extends ViewModel, exposes StateFlow<UiState>
  <Feature>UiState.kt     // data class UiState(...)
```

## Rules
- Screen composable is stateless: `val state by viewModel.uiState.collectAsStateWithLifecycle()`.
- All events are lambdas passed down, never direct VM calls from deep composables.
- Minimum touch target 48 dp for every interactive element.
- All text via `stringResource(R.string.*)` — no hardcoded strings.
- No hardcoded colors; use `MaterialTheme.colorScheme.*`.
- Preview function provides both light and dark themes:
  ```kotlin
  @Preview(uiMode = UI_MODE_NIGHT_NO) @Preview(uiMode = UI_MODE_NIGHT_YES)
  @Composable fun <Feature>ScreenPreview() { AppTheme { <Feature>Screen(...) } }
  ```
- Match `docs/screens/` PNGs for layout, colors, spacing. Use `ui-reviewer` subagent to diff.

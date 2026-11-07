# LearningTracker

- Daily learning journal app: log hours, tasks, mood, focus, energy each day.
- Stack: Kotlin, Jetpack Compose, Material 3, Room, DataStore. Package: `uz.learningtracker`. minSdk 26.

## Architecture
- Layers: `data/` (Room, DataStore, mappers) → `domain/` (models, repo interfaces, use-cases; zero Android imports) → `ui/` (screens, ViewModels).
- Each ViewModel exposes exactly one `StateFlow<UiState>`.
- DI: manual `AppContainer` in `Application`; no Hilt.
- Storage: Room for all records; DataStore only for theme preference.

## Code rules
- Small files and functions; no dead code; no TODO placeholders.
- State hoisting: composables are stateless, receive state + lambdas.
- No hardcoded strings or colors in composables; all UI text via `stringResource` from `strings.xml` in Uzbek Latin.
- See skills: `compose-screen`, `room-data-layer`, `uzbek-strings`, `verify-task`.

## Workflow
- One phase per session. At session start: read `docs/PROGRESS.md`, find first phase not DONE, read `docs/DESIGN.md` and that phase's screens (see TASKS.md), start from first unfinished step.
- After each step: build → fix → update `docs/PROGRESS.md` → `git commit "phase N step M: title"` → continue immediately.
- Phase done: build + tests → mark DONE in `docs/PROGRESS.md` → `git commit "phase N done"` → print `"PHASE N DONE. Run /clear, then start phase N+1."` → STOP.

## Token rules
- Never read `build/`, `.gradle/`, `.idea/`, `*.png`, `*.apk` unless a task explicitly requires it.
- `docs/screens/` images: read only during the phase listed for them in `docs/TASKS.md`.
- Prefer targeted Grep/Read with line ranges over whole-file reads; do not re-read files just written.
- Keep chat output short; no large code blocks in chat.

## Docs
- `docs/DESIGN.md` — visual spec and component guide
- `docs/TASKS.md` — ordered task list with phase rules
- `docs/PROGRESS.md` — per-phase completion log
- `docs/DECISIONS.md` — architectural decisions

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
- Read `docs/PROGRESS.md` first; resume from first unfinished task in `docs/TASKS.md`.
- After each task: build → fix → update `docs/PROGRESS.md` → `git commit "task N: title"`.

## Token rules
- Never read `build/`, `.gradle/`, `.idea/`, `*.png`, `*.apk` unless a task explicitly requires it.
- `docs/screens/` images: only via `ui-reviewer` subagent or when building that exact screen.
- Prefer targeted Grep/Read with line ranges over whole-file reads; do not re-read files just written.
- Keep chat output short; no large code blocks in chat.

## Docs
- `docs/DESIGN.md` — visual spec and component guide
- `docs/TASKS.md` — ordered task list
- `docs/PROGRESS.md` — per-task completion log
- `docs/DECISIONS.md` — architectural decisions

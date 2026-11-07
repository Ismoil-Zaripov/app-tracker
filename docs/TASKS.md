# Learning Tracker — Build Tasks

## Phase rules
- Work on ONE phase per session. Never start the next phase in the same session.
- Inside a phase, after each step: build, fix errors, update `docs/PROGRESS.md`, `git commit "phase N step M: title"`, continue without asking.
- When all steps of a phase are done: run build and unit tests, mark phase DONE in `docs/PROGRESS.md`, commit `"phase N done"`, print exactly `"PHASE N DONE. Run /clear, then start phase N+1."` and STOP.
- At session start: read `CLAUDE.md` and `docs/PROGRESS.md`, find the first phase not DONE, read `docs/DESIGN.md` and only the screens listed for that phase, start from the first unfinished step.
- If blocked after 3 attempts: mark BLOCKED with reason in `docs/PROGRESS.md`, skip to the next independent step, return at end of phase.

## Screens by phase
- Phase 1: docs/screens/ 01–03 (Bugungi yozuv light/dark/edit)
- Phase 2: docs/screens/ 07–09 (Tarix light/delete dialog/empty)
- Phase 3: docs/screens/ 04–06 (Dashboard light/dark/not-entered)
- Phase 4: docs/screens/ 10–12 (Haftalik hisobot light/dark/empty)
- Phase 5: all screens via `ui-reviewer` subagent only

## Inputs
- Screenshots: docs/screens/ (01..12 PNG: Bugungi yozuv light/dark/edit, Dashboard light/dark/not-entered, Tarix light/delete dialog/empty, Haftalik hisobot light/dark/empty).
- Design system: docs/DESIGN.md. Read it before any UI code. UI must closely match the screenshots (colors, spacing, radius, cards, icons, bottom nav). Extract exact colors into the Theme.

## Stack
Kotlin, Jetpack Compose, Material 3, Navigation Compose, Room (KSP), ViewModel + StateFlow, java.time (desugaring if needed), DataStore Preferences (theme only). Charts: Compose Canvas (no chart library). Version catalog (libs.versions.toml). minSdk 26, targetSdk latest stable. Package uz.learningtracker. Single Activity. DI: manual AppContainer in Application, ViewModel factories (no Hilt/Koin).

## Architecture
- data: Room (Entity, Dao, Database), mappers, RepositoryImpl, DataStore settings
- domain: models, Repository interfaces, small use cases (no Android imports)
- ui: theme, navigation, components, screens (Screen + ViewModel + UiState each)
  ViewModels expose one immutable `StateFlow<UiState>`; composables are stateless where possible.

## Data model
DailyEntry: id, date (LocalDate, UNIQUE), hoursWorked (Double), tasksCompleted (Int), mainTopic, understanding (1-5), energy (1-5), focus (1-5), biggestWin, biggestDifficulty, createdAt, updatedAt.
One entry per date: saving for an existing date updates it (edit mode).

## Business rules
- Streak: consecutive days with an entry ending today; if today has none but yesterday does, streak is still alive (counts up to yesterday).
- Averages of energy/focus/understanding for last 7 and 30 days.
- Total hours this week (Monday–Sunday) and this month.
- Weekly report for a selected week: total hours, total tasks, avg energy/focus/understanding, top 3 wins and top 3 difficulties (wins: highest energy/focus; difficulties: lowest; skip blank texts), hours per weekday.
- Validation: hours decimal 0..24 (accept "2.5" and "2,5"), tasks >= 0, ratings default 3, topic required. Errors in Uzbek.

---

## Phase 0 — Foundation

**Goal:** Working app shell with navigation, theme, and Room ready; all 4 screens are placeholders.

1. Adapt Gradle and version catalog; add all dependencies (Compose, Material 3, Navigation, Room + KSP, DataStore, java.time desugaring).
2. Create package structure: `data/`, `domain/`, `ui/`; add `AppContainer` in `Application`.
3. Theme: M3 light/dark color schemes from DESIGN.md, typography, shapes, spacing constants.
4. DataStore theme preference (`ThemeSettingsRepository`); `ThemeMode` enum (SYSTEM/LIGHT/DARK).
5. `strings.xml` base: app name, bottom nav labels (Bugun, Dashboard, Tarix, Hisobot), theme option labels.
6. Navigation shell: `NavHost`, `BottomNavBar`, `TopBar` with settings icon, theme bottom sheet.
7. Placeholder screens for all 4 destinations wired into the nav graph.
8. Room: `DailyEntry` entity (all fields with UNIQUE date constraint), `DailyEntryDao` (upsert, getByDate, getAll, observeAll, delete), `AppDatabase`.
9. `DailyRepository` interface (domain), `DailyRepositoryImpl` (data); wire into `AppContainer`.

**Needs:** nothing (first phase).

**Done when:** app builds and runs, navigation between all 4 tabs works, theme toggle persists across restarts, Room compiles without errors.

---

## Phase 1 — Feature "Bugungi yozuv"

**Goal:** Complete entry form: save new entries, auto-switch to edit mode for an existing date.

1. Domain model `DailyEntry`; use cases `SaveEntry` and `GetEntryByDate`; validation logic (hours 0–24 "2.5"/"2,5", tasks ≥ 0, ratings default 3, topic required, Uzbek error strings).
2. Shared form components: `RatingSelector` (1–5), `LabeledTextField` with inline error, `DatePickerField`.
3. `EntryUiState` and `EntryViewModel` (loads entry for date, decides new vs edit, calls `SaveEntry`).
4. `BugungiYozuvScreen`: all fields (hours, tasks, mainTopic, understanding/energy/focus, biggestWin, biggestDifficulty, date), `Saqlash` button; title "Yozuvni tahrirlash" in edit mode.
5. Accept optional `date` nav argument; default to today.
6. Snackbar on successful save; navigate back if opened from Tarix.
7. Unit tests for all validation rules.
8. Unit tests for `SaveEntry` upsert behavior (new entry created, existing entry updated).

**Needs:** Phase 0 (AppContainer, Room, navigation, theme).
**Screens:** docs/screens/ 01–03.

**Done when:** build passes, entry can be saved and re-opened with existing values, errors shown in Uzbek, unit tests pass.

---

## Phase 2 — Feature "Tarix"

**Goal:** History list with date-range filter, tap-to-edit, and delete with confirmation.

1. Use cases `ObserveEntries` (optional date range filter) and `DeleteEntry`.
2. `HistoryUiState` and `HistoryViewModel`.
3. `TarixScreen`: entry list newest-first with `EntryListItem` composable.
4. Date-range filter: start/end date pickers, active filter chip with clear button.
5. Tap item → navigate to Phase 1 screen with that entry's date argument.
6. Delete icon → `ConfirmDeleteDialog`; on confirm call `DeleteEntry`.
7. `EmptyState` composable (reusable) shown when list is empty or filter matches nothing.
8. Unit tests for `ObserveEntries` with and without filter, `DeleteEntry`.

**Needs:** Phase 0 (Room, navigation), Phase 1 (entry screen, shared form components).
**Screens:** docs/screens/ 07–09.

**Done when:** build passes, list shows entries, filter narrows results, edit and delete work end-to-end, empty state appears, tests pass.

---

## Phase 3 — Feature "Dashboard"

**Goal:** Dashboard with streak, stat cards, 7/30-day toggle, and line chart.

1. Use case `GetDashboardStats`: streak (rules above), 7-day and 30-day averages for energy/focus/understanding, total hours this week and this month. Returns `DashboardStats` domain model.
2. Shared composables `StatCard` and `SectionCard`.
3. `DashboardUiState` and `DashboardViewModel`.
4. `DashboardScreen`: streak card, today status (green check or "Bugun yozuv kiritilmagan" + button to entry screen), 7/30 toggle, stat cards for averages, week and month hours.
5. `LineChart` composable (Canvas) for energy and focus trend over selected period.
6. Unit tests for streak (consecutive days, today missing, yesterday present, single day, empty data) and averages (normal, gaps, week boundary).

**Needs:** Phase 0 (Room), Phase 1 (entry screen for the button), Phase 2 (EmptyState).
**Screens:** docs/screens/ 04–06.

**Done when:** build passes, stats are correct for real data, chart renders, today-missing state shows with correct button, tests pass.

---

## Phase 4 — Feature "Haftalik hisobot"

**Goal:** Weekly report with week navigator, summary cards, bar chart, and top wins/difficulties.

1. Use case `GetWeeklyReport`: for a Monday–Sunday week — total hours, total tasks, avg energy/focus/understanding, top 3 wins (non-blank `biggestWin`, highest energy+focus), top 3 difficulties (non-blank `biggestDifficulty`, lowest), hours per weekday array. Returns `WeeklyReport` domain model.
2. `WeeklyReportUiState` and `WeeklyReportViewModel` (current week default, prev/next navigation).
3. `WeekSelector` composable (← label →) showing e.g. "4–10 Nov 2024".
4. Summary cards (total hours, total tasks, avg ratings) using `StatCard`.
5. `BarChart` composable (Canvas) for hours per weekday.
6. Top wins and difficulties section (up to 3 each, skip blank).
7. `EmptyState` for weeks with no entries.
8. Unit tests for `GetWeeklyReport`: normal week, empty week, partial week, top-3 logic, week boundary (Mon–Sun).

**Needs:** Phase 0 (Room), Phase 3 (StatCard).
**Screens:** docs/screens/ 10–12.

**Done when:** build passes, week selector navigates correctly, report data is accurate, bar chart renders, empty state appears for empty weeks, tests pass.

---

## Phase 5 — Polish and release check

**Goal:** Every screen matches screenshots; app is accessible, robust, and lint-clean.

1. `ui-reviewer` subagent: compare Phase 1 screens (01–03), fix all visual differences.
2. `ui-reviewer` subagent: compare Phase 2 screens (07–09), fix differences.
3. `ui-reviewer` subagent: compare Phase 3 screens (04–06), fix differences.
4. `ui-reviewer` subagent: compare Phase 4 screens (10–12), fix differences.
5. Dark theme: open every screen in dark mode and fix color issues.
6. Touch targets: every tappable element ≥ 48 dp.
7. IME: all text fields use correct keyboard type (decimal, number, text); IME action advances focus or saves.
8. State survival: verify every screen restores state after rotation and process death.
9. Accessibility: `contentDescription` on every icon-only button; no hardcoded strings or colors in composables.
10. Run `lint`; fix all errors and warnings.
11. Run unit tests; all pass.
12. Remove unused code, unused imports, unused resources.
13. Write short `README.md` (what the app does, architecture, how to build and run).

**Needs:** all phases complete.

**Done when:** build clean, lint clean, all unit tests pass, every screen matches screenshots in both themes, README written.

---

## Definition of done
App builds and runs; 4 screens work end to end with real Room data; all UI text Uzbek; light and dark match screenshots; unit tests pass; PROGRESS.md shows all phases DONE.

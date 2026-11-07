# Progress

## Phase 0 — Foundation `DONE`
- [x] Step 1: Gradle, version catalog, dependencies
- [x] Step 2: Package structure, AppContainer
- [x] Step 3: Theme (M3 colors, typography, shapes)
- [x] Step 4: DataStore theme preference
- [x] Step 5: strings.xml base
- [x] Step 6: Navigation shell (NavHost, bottom nav, top bar, theme sheet)
- [x] Step 7: Placeholder screens
- [x] Step 8: Room (entity, DAO, database)
- [x] Step 9: Repository interface and impl

## Phase 1 — Bugungi yozuv `DONE`
- [x] Step 1: Domain model, SaveEntry, GetEntryByDate, validation
- [x] Step 2: RatingSelector, LabeledTextField, DatePickerField
- [x] Step 3: EntryUiState, EntryViewModel
- [x] Step 4: BugungiYozuvScreen (all fields, edit mode)
- [x] Step 5: Date nav argument, default today
- [x] Step 6: Snackbar on success, back navigation
- [x] Step 7: Unit tests for validation
- [x] Step 8: Unit tests for SaveEntry upsert

## Phase 2 — Tarix `DONE`
- [x] Step 1: ObserveEntries, DeleteEntry use cases
- [x] Step 2: HistoryUiState, HistoryViewModel
- [x] Step 3: TarixScreen list with EntryListItem
- [x] Step 4: Date-range filter with clear chip
- [x] Step 5: Tap to edit (navigate to entry screen)
- [x] Step 6: ConfirmDeleteDialog and delete
- [x] Step 7: EmptyState composable
- [x] Step 8: Unit tests

## Phase 3 — Dashboard `DONE`
- [x] Step 1: GetDashboardStats use case
- [x] Step 2: StatCard, SectionCard composables
- [x] Step 3: DashboardUiState, DashboardViewModel
- [x] Step 4: DashboardScreen (streak, today status, 7/30 toggle, stat cards)
- [x] Step 5: LineChart (Canvas)
- [x] Step 6: Unit tests for streak and averages

## Phase 4 — Haftalik hisobot `IN PROGRESS`
- [x] Step 1: GetWeeklyReport use case
- [x] Step 2: WeeklyReportUiState, WeeklyReportViewModel
- [ ] Step 3: WeekSelector composable
- [ ] Step 4: Summary cards
- [ ] Step 5: BarChart (Canvas)
- [ ] Step 6: Top wins and difficulties section
- [ ] Step 7: EmptyState for empty weeks
- [ ] Step 8: Unit tests

## Phase 5 — Polish `NOT STARTED`
- [ ] Step 1: ui-reviewer Phase 1 screens (01–03)
- [ ] Step 2: ui-reviewer Phase 2 screens (07–09)
- [ ] Step 3: ui-reviewer Phase 3 screens (04–06)
- [ ] Step 4: ui-reviewer Phase 4 screens (10–12)
- [ ] Step 5: Dark theme check all screens
- [ ] Step 6: Touch targets ≥ 48 dp
- [ ] Step 7: IME handling
- [ ] Step 8: State survival (rotation, process death)
- [ ] Step 9: Accessibility (contentDescription, no hardcoded strings/colors)
- [ ] Step 10: Lint clean
- [ ] Step 11: Unit tests all pass
- [ ] Step 12: Remove unused code
- [ ] Step 13: README.md

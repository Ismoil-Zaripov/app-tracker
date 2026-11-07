# Learning Tracker - Design Spec ("Teal Sanctuary")

Compact spec for Compose implementation. Material 3, calm, minimal, tonal surfaces (no heavy shadows). Screens: docs/screens/*.png.

## Important notes
- The Stitch screenshots named "qorong'i" are NOT truly dark (they render with the light palette). Use them only for LAYOUT. Dark theme colors come from the Dark palette below.
- Screenshots contain extra decorative texts/sections (quotes, "Rekord", insight cards, "Maqsad sari"). Implement only: features from docs/TASKS.md plus visual structure. Optional extras (Rekord = longest streak) may be skipped. Texts come from strings.xml (see uzbek-strings skill), not from screenshots.
- Screenshot text varies between screens (e.g. "Ruhiy quvvat" vs "Energiya"); always use the app terms from the uzbek-strings skill.

## Colors - Light
| role | hex |
|---|---|
| primary | #005050 |
| onPrimary | #FFFFFF |
| primaryContainer | #006A6A |
| onPrimaryContainer | #97E7E6 |
| inversePrimary | #84D4D3 |
| secondary | #4A6363 |
| onSecondary | #FFFFFF |
| secondaryContainer | #CCE8E7 |
| onSecondaryContainer | #506969 |
| tertiary | #70371A |
| onTertiary | #FFFFFF |
| tertiaryContainer | #8D4E2F |
| onTertiaryContainer | #FFCFBA |
| error | #BA1A1A |
| onError | #FFFFFF |
| errorContainer | #FFDAD6 |
| onErrorContainer | #93000A |
| background / surface | #F7FAF9 |
| onBackground / onSurface | #181C1C |
| surfaceVariant / surfaceContainerHighest | #E0E3E2 |
| onSurfaceVariant | #3E4948 |
| surfaceDim | #D7DBDA |
| surfaceContainerLowest | #FFFFFF |
| surfaceContainerLow | #F1F4F3 |
| surfaceContainer | #EBEEEE |
| surfaceContainerHigh | #E6E9E8 |
| outline | #6E7979 |
| outlineVariant | #BEC9C8 |
| inverseSurface | #2D3131 |
| inverseOnSurface | #EEF1F0 |
| surfaceTint | #006A6A |

## Colors - Dark
| role | hex |
|---|---|
| primary | #4DDAD8 |
| onPrimary | #003737 |
| primaryContainer | #004F4F |
| onPrimaryContainer | #CCE8E6 |
| secondary | #B0CCCB |
| onSecondary | #1B3535 |
| secondaryContainer | #324B4B |
| onSecondaryContainer | #CCE8E7 |
| tertiary | #FFB694 |
| onTertiary | #351000 |
| tertiaryContainer | #70371A |
| onTertiaryContainer | #FFDBCC |
| error | #FFB4AB |
| onError | #690005 |
| errorContainer | #93000A |
| onErrorContainer | #FFDAD6 |
| background / surface | #191C1B |
| onBackground / onSurface | #E0E3E1 |
| onSurfaceVariant | #BEC8C7 |
| surfaceDim | #111413 |
| surfaceContainerLowest | #0C0F0E |
| surfaceContainerLow | #1D2020 |
| surfaceContainer | #232726 |
| surfaceContainerHigh | #2D3231 |
| surfaceContainerHighest / surfaceVariant | #383D3C |
| outline | #899392 |
| outlineVariant | #3F4948 |
| inverseSurface | #E0E3E1 |
| inverseOnSurface | #2D3131 |

## Data-viz colors
- Fokus line/ring: primary (light #006A6A, dark #4DDAD8)
- Energiya line/ring: tertiary (light #8D4E2F, dark #FFB694)
- Tushunish ring: secondary (light #4A6363, dark #B0CCCB)
- Chart area fill: line color at ~12% alpha; grid lines: outlineVariant; axis labels: onSurfaceVariant (label-small)
- Rings/bars track: surfaceContainerHighest

## Typography (Inter; fall back to default sans if font not bundled)
| style | size / line / weight |
|---|---|
| displayLarge | 56 / 64 / 400 |
| displayMedium | 45 / 52 / 400 |
| headlineLarge | 32 / 40 / 600 |
| headlineMedium | 28 / 36 / 600 |
| headlineSmall | 24 / 32 / 600 |
| titleLarge | 22 / 28 / 500 |
| titleMedium | 16 / 24 / 600 |
| titleSmall | 14 / 20 / 500 |
| bodyLarge | 16 / 24 / 400 |
| bodyMedium | 14 / 20 / 400 |
| bodySmall | 12 / 16 / 400 |
| labelLarge | 14 / 20 / 600 |
| labelMedium | 12 / 16 / 500 |
| labelSmall | 11 / 14 / 500 |
Uzbek text (o', g', sh, ch) needs line-height >= 1.4x on body text; never clip diacritics. Do not fix widths of chips/buttons (long suffixes).

## Shapes
- Chips, rating circles, primary button: fully rounded (pill)
- Text fields: 16dp
- Cards / dialogs / sheets: 20-24dp
- Bottom nav active indicator: 64x32dp pill

## Spacing
Grid 8dp (4dp micro steps). Screen margin 16dp (20dp on width >= 412dp). Gutter 16dp. Section gap 16-24dp. Card padding 16dp.
xs 4, sm 8, md 16, lg 24, xl 32.

## Elevation (tonal)
- L0 screen: surface
- L1 cards/inputs: surfaceContainer (or surfaceContainerLow); no shadow
- L2 top bar on scroll, bottom nav: surfaceContainerHigh + soft shadow
- L3 dialogs/menus/sheets: surfaceContainerHighest + soft shadow

## Components
- Primary button "Saqlash": 56dp high, full width, pill, primary fill, onPrimary text, labelLarge. Disabled: onSurface 12% fill, 38% text. Sticky at bottom of the entry screen.
- OutlinedTextField: min 56dp, radius 16dp, 1dp outline (inactive), 2dp primary (focused), bodyLarge text, error state with supporting text (error color).
- Rating selector 1-5 (tushunish, energiya, fokus): 48x48dp circles, 8dp gap. Inactive: surfaceContainer + 1dp outlineVariant + onSurfaceVariant text (titleMedium). Active: primary fill + onPrimary text. Group the three selectors inside one card.
- Segmented toggle ("7 kun" / "30 kun"): 40dp high, pill; selected = secondaryContainer + check icon; unselected transparent with outline dividers.
- Bottom navigation: 80dp + insets, surfaceContainer; 4 items Bugun (edit_note / calendar_today), Dashboard (insights), Tarix (history), Hisobot (pie_chart / calendar-week). Active: pill indicator secondaryContainer, icon onSecondaryContainer, label labelMedium semibold. Inactive: onSurfaceVariant.
- Top app bar: 64dp, title titleLarge (screen name), right action = palette/theme icon -> modal bottom sheet with "Tizimga mos", "Yorug'", "Qorong'i" (selected one marked).
- Streak hero card (Dashboard): primaryContainer-based gradient (dark: primaryContainer), big number (displayMedium) + "kun", radius 24dp.
- Status card (Bugungi yozuv): done = chip "Bajarilgan" (check icon, secondaryContainer) + summary chips (soat, task, energiya); not done = text "Bugun yozuv kiritilmagan" + button "Yozuv qo'shish".
- Stat cards: surfaceContainer, radius 20dp, label (bodySmall, onSurfaceVariant) + value (headlineSmall).
- History entry card: surfaceContainerLow, 20dp, padding 16dp: date + topic (titleMedium), hours/tasks chips (secondaryContainer), 3 small rating badges (energiya, fokus, tushunish). Delete dialog: radius 28dp, buttons "Bekor qilish" / "O'chirish" (error color).
- Empty states: centered simple icon/illustration (outline-toned, no external image), title + one supporting line.
- Icons: Material Symbols (Outlined) via androidx material-icons (use extended only if needed; prefer the core set to keep APK small).

## Screens (layout order)
1. Bugun: top bar -> date field -> hours + tasks (2 columns) -> topic -> ratings card (tushunish, energiya, fokus) -> g'alaba (3 lines) -> qiyinchilik (3 lines) -> sticky Saqlash. Edit mode: title "Yozuvni tahrirlash", fields prefilled.
2. Dashboard: streak hero -> today status -> averages card with 7/30 toggle (3 metrics with rings) -> hours cards (Bu hafta | Bu oy, 2 columns) -> energy/focus line chart card.
3. Tarix: filter chip row ("Sana bo'yicha filter", active chip with x) -> entry list newest first -> empty state.
4. Hisobot: week selector (< range >) -> 2x2 summary grid -> hours-per-weekday bar chart -> "Eng yaxshi g'alabalar" (top 3) -> "Eng katta qiyinchiliklar" (top 3) -> empty state.

## Accessibility
Contrast >= 4.5:1 body, 3:1 large text; touch targets >= 48dp; contentDescription on all icon-only buttons; do not convey rating/state by color alone.
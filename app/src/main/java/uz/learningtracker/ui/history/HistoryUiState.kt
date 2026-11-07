package uz.learningtracker.ui.history

import uz.learningtracker.domain.model.DailyEntry
import java.time.LocalDate

data class HistoryUiState(
    val entries: List<DailyEntry> = emptyList(),
    val filterFrom: LocalDate? = null,
    val filterTo: LocalDate? = null,
    val entryToDelete: DailyEntry? = null,
    val isLoading: Boolean = true,
)

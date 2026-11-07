package uz.learningtracker.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import uz.learningtracker.domain.usecase.GetWeeklyReport
import uz.learningtracker.domain.usecase.ObserveEntries
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class WeeklyReportViewModel(
    observeEntries: ObserveEntries,
) : ViewModel() {

    private val _weekStart = MutableStateFlow(currentMonday())

    val uiState = combine(
        _weekStart,
        observeEntries(),
    ) { weekStart, entries ->
        WeeklyReportUiState(
            weekStart = weekStart,
            report = GetWeeklyReport.compute(entries, weekStart),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        WeeklyReportUiState(weekStart = currentMonday()),
    )

    fun previousWeek() = _weekStart.update { it.minusWeeks(1) }
    fun nextWeek() = _weekStart.update { it.plusWeeks(1) }
    fun goToCurrentWeek() = _weekStart.update { currentMonday() }

    class Factory(private val observeEntries: ObserveEntries) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            WeeklyReportViewModel(observeEntries) as T
    }

    companion object {
        fun currentMonday(today: LocalDate = LocalDate.now()): LocalDate =
            today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }
}

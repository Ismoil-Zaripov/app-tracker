package uz.learningtracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uz.learningtracker.domain.usecase.GetDashboardStats
import uz.learningtracker.domain.usecase.ObserveEntries
import java.time.LocalDate

class DashboardViewModel(
    observeEntries: ObserveEntries,
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(7)

    val uiState = combine(
        observeEntries().map { entries -> GetDashboardStats.compute(entries, LocalDate.now()) },
        _selectedPeriod,
    ) { stats, period ->
        DashboardUiState(stats = stats, selectedPeriod = period)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DashboardUiState(),
    )

    fun selectPeriod(days: Int) { _selectedPeriod.value = days }

    class Factory(private val observeEntries: ObserveEntries) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DashboardViewModel(observeEntries) as T
    }
}

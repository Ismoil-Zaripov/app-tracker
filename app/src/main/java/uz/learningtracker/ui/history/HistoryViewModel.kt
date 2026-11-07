package uz.learningtracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.usecase.DeleteEntry
import uz.learningtracker.domain.usecase.ObserveEntries
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModel(
    private val observeEntries: ObserveEntries,
    private val deleteEntry: DeleteEntry,
) : ViewModel() {

    private val filterFrom = MutableStateFlow<LocalDate?>(null)
    private val filterTo = MutableStateFlow<LocalDate?>(null)
    private val entryToDelete = MutableStateFlow<DailyEntry?>(null)

    private val entriesFlow = combine(filterFrom, filterTo) { from, to -> from to to }
        .flatMapLatest { (from, to) -> observeEntries(from, to) }

    val uiState: StateFlow<HistoryUiState> = combine(
        entriesFlow,
        filterFrom,
        filterTo,
        entryToDelete,
    ) { entries, from, to, toDelete ->
        HistoryUiState(
            entries = entries,
            filterFrom = from,
            filterTo = to,
            entryToDelete = toDelete,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState(),
    )

    fun setFilter(from: LocalDate?, to: LocalDate?) {
        filterFrom.value = from
        filterTo.value = to
    }

    fun clearFilter() {
        filterFrom.value = null
        filterTo.value = null
    }

    fun requestDelete(entry: DailyEntry) {
        entryToDelete.value = entry
    }

    fun cancelDelete() {
        entryToDelete.value = null
    }

    fun confirmDelete() {
        val entry = entryToDelete.value ?: return
        viewModelScope.launch {
            deleteEntry(entry)
            entryToDelete.value = null
        }
    }

    class Factory(
        private val observeEntries: ObserveEntries,
        private val deleteEntry: DeleteEntry,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HistoryViewModel(observeEntries, deleteEntry) as T
    }
}

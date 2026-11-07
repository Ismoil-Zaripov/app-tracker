package uz.learningtracker.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.usecase.GetEntryByDate
import uz.learningtracker.domain.usecase.SaveEntry
import uz.learningtracker.domain.usecase.parseHours
import uz.learningtracker.domain.usecase.parseTasks
import java.time.LocalDate

class EntryViewModel(
    private val saveEntry: SaveEntry,
    private val getEntryByDate: GetEntryByDate,
    private val initialDate: LocalDate,
    private val hoursRequiredError: String,
    private val hoursInvalidError: String,
    private val tasksRequiredError: String,
    private val tasksInvalidError: String,
    private val topicRequiredError: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EntryUiState(date = initialDate))
    val uiState: StateFlow<EntryUiState> = _uiState.asStateFlow()

    init {
        loadEntry(initialDate)
    }

    private fun loadEntry(date: LocalDate) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val existing = getEntryByDate(date)
            if (existing != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditMode = true,
                        lastUpdatedAt = existing.updatedAt,
                        hoursText = existing.hoursWorked.let { h ->
                            if (h == h.toLong().toDouble()) h.toLong().toString() else h.toString()
                        },
                        tasksText = existing.tasksCompleted.toString(),
                        mainTopic = existing.mainTopic,
                        understanding = existing.understanding,
                        energy = existing.energy,
                        focus = existing.focus,
                        biggestWin = existing.biggestWin,
                        biggestDifficulty = existing.biggestDifficulty,
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, isEditMode = false) }
            }
        }
    }

    fun onDateChange(date: LocalDate) {
        _uiState.update { EntryUiState(date = date) }
        loadEntry(date)
    }

    fun onHoursChange(text: String) = _uiState.update { it.copy(hoursText = text, hoursError = null) }
    fun onTasksChange(text: String) = _uiState.update { it.copy(tasksText = text, tasksError = null) }
    fun onTopicChange(text: String) = _uiState.update { it.copy(mainTopic = text, topicError = null) }
    fun onUnderstandingChange(v: Int) = _uiState.update { it.copy(understanding = v) }
    fun onEnergyChange(v: Int) = _uiState.update { it.copy(energy = v) }
    fun onFocusChange(v: Int) = _uiState.update { it.copy(focus = v) }
    fun onWinChange(text: String) = _uiState.update { it.copy(biggestWin = text) }
    fun onDifficultyChange(text: String) = _uiState.update { it.copy(biggestDifficulty = text) }

    fun onSave() {
        val state = _uiState.value
        val hoursError = when {
            state.hoursText.isBlank() -> hoursRequiredError
            parseHours(state.hoursText) == null -> hoursInvalidError
            else -> null
        }
        val tasksError = when {
            state.tasksText.isBlank() -> tasksRequiredError
            parseTasks(state.tasksText) == null -> tasksInvalidError
            else -> null
        }
        val topicError = if (state.mainTopic.isBlank()) topicRequiredError else null

        if (hoursError != null || tasksError != null || topicError != null) {
            _uiState.update { it.copy(hoursError = hoursError, tasksError = tasksError, topicError = topicError) }
            return
        }

        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val existing = getEntryByDate(state.date)
            val entry = DailyEntry(
                id = existing?.id ?: 0L,
                date = state.date,
                hoursWorked = parseHours(state.hoursText)!!,
                tasksCompleted = parseTasks(state.tasksText)!!,
                mainTopic = state.mainTopic.trim(),
                understanding = state.understanding,
                energy = state.energy,
                focus = state.focus,
                biggestWin = state.biggestWin.trim(),
                biggestDifficulty = state.biggestDifficulty.trim(),
                createdAt = existing?.createdAt ?: now,
                updatedAt = now,
            )
            saveEntry(entry)
            _uiState.update { it.copy(savedEvent = true, isEditMode = true, lastUpdatedAt = now) }
        }
    }

    fun onSaveEventConsumed() = _uiState.update { it.copy(savedEvent = false) }

    class Factory(
        private val saveEntry: SaveEntry,
        private val getEntryByDate: GetEntryByDate,
        private val initialDate: LocalDate,
        private val hoursRequiredError: String,
        private val hoursInvalidError: String,
        private val tasksRequiredError: String,
        private val tasksInvalidError: String,
        private val topicRequiredError: String,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            EntryViewModel(
                saveEntry, getEntryByDate, initialDate,
                hoursRequiredError, hoursInvalidError,
                tasksRequiredError, tasksInvalidError, topicRequiredError,
            ) as T
    }
}

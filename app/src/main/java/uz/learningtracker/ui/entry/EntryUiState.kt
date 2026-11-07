package uz.learningtracker.ui.entry

import java.time.LocalDate

data class EntryUiState(
    val date: LocalDate = LocalDate.now(),
    val hoursText: String = "",
    val tasksText: String = "",
    val mainTopic: String = "",
    val understanding: Int = 3,
    val energy: Int = 3,
    val focus: Int = 3,
    val biggestWin: String = "",
    val biggestDifficulty: String = "",
    val hoursError: String? = null,
    val tasksError: String? = null,
    val topicError: String? = null,
    val isEditMode: Boolean = false,
    val lastUpdatedAt: Long? = null,
    val isLoading: Boolean = false,
    val savedEvent: Boolean = false,
)

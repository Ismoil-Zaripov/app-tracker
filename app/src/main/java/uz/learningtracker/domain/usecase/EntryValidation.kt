package uz.learningtracker.domain.usecase

data class EntryInput(
    val date: java.time.LocalDate,
    val hoursText: String,
    val tasksText: String,
    val mainTopic: String,
    val understanding: Int,
    val energy: Int,
    val focus: Int,
    val biggestWin: String,
    val biggestDifficulty: String,
)

data class EntryValidationErrors(
    val hoursError: String? = null,
    val tasksError: String? = null,
    val topicError: String? = null,
) {
    val hasErrors: Boolean get() = hoursError != null || tasksError != null || topicError != null
}

fun parseHours(text: String): Double? {
    val normalized = text.trim().replace(',', '.')
    return normalized.toDoubleOrNull()?.takeIf { it in 0.0..24.0 }
}

fun parseTasks(text: String): Int? {
    return text.trim().toIntOrNull()?.takeIf { it >= 0 }
}

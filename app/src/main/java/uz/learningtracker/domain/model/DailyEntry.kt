package uz.learningtracker.domain.model

import java.time.LocalDate

data class DailyEntry(
    val id: Long = 0,
    val date: LocalDate,
    val hoursWorked: Double,
    val tasksCompleted: Int,
    val mainTopic: String,
    val understanding: Int,
    val energy: Int,
    val focus: Int,
    val biggestWin: String,
    val biggestDifficulty: String,
    val createdAt: Long,
    val updatedAt: Long,
)

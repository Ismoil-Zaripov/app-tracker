package uz.learningtracker.domain.model

import java.time.LocalDate

data class ReportItem(val date: LocalDate, val text: String)

data class WeeklyReport(
    val weekStart: LocalDate,
    val weekEnd: LocalDate,
    val totalHours: Double,
    val totalTasks: Int,
    val avgEnergy: Double,
    val avgFocus: Double,
    val avgUnderstanding: Double,
    val topWins: List<ReportItem>,
    val topDifficulties: List<ReportItem>,
    val hoursPerWeekday: List<Double>,
)

package uz.learningtracker.domain.model

import java.time.LocalDate

data class DashboardStats(
    val streak: Int,
    val todayEntry: DailyEntry?,
    val period7: PeriodStats,
    val period30: PeriodStats,
    val hoursThisWeek: Double,
    val hoursThisMonth: Double,
) {
    val hasTodayEntry: Boolean get() = todayEntry != null
}

data class PeriodStats(
    val avgEnergy: Double,
    val avgFocus: Double,
    val avgUnderstanding: Double,
    val energyPoints: List<Pair<LocalDate, Int>>,
    val focusPoints: List<Pair<LocalDate, Int>>,
)

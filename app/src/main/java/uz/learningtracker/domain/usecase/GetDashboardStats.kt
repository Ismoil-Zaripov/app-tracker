package uz.learningtracker.domain.usecase

import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.model.DashboardStats
import uz.learningtracker.domain.model.PeriodStats
import uz.learningtracker.domain.repository.DailyRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class GetDashboardStats(private val repository: DailyRepository) {

    suspend operator fun invoke(today: LocalDate = LocalDate.now()): DashboardStats =
        compute(repository.getAll(), today)

    companion object {
        fun compute(entries: List<DailyEntry>, today: LocalDate = LocalDate.now()): DashboardStats {
            val dates = entries.map { it.date }.toSet()
            val todayEntry = entries.firstOrNull { it.date == today }
            val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val firstOfMonth = today.withDayOfMonth(1)
            val hoursThisWeek = entries
                .filter { !it.date.isBefore(monday) && !it.date.isAfter(today) }
                .sumOf { it.hoursWorked }
            val hoursThisMonth = entries
                .filter { !it.date.isBefore(firstOfMonth) && !it.date.isAfter(today) }
                .sumOf { it.hoursWorked }
            return DashboardStats(
                streak = calculateStreak(dates, today),
                todayEntry = todayEntry,
                period7 = computePeriodStats(entries, today, 7),
                period30 = computePeriodStats(entries, today, 30),
                hoursThisWeek = hoursThisWeek,
                hoursThisMonth = hoursThisMonth,
            )
        }

        fun calculateStreak(dates: Set<LocalDate>, today: LocalDate): Int {
            val start = when {
                today in dates -> today
                today.minusDays(1) in dates -> today.minusDays(1)
                else -> return 0
            }
            var streak = 0
            var current = start
            while (current in dates) {
                streak++
                current = current.minusDays(1)
            }
            return streak
        }

        fun computePeriodStats(entries: List<DailyEntry>, today: LocalDate, days: Int): PeriodStats {
            val from = today.minusDays(days.toLong() - 1)
            val period = entries
                .filter { !it.date.isBefore(from) && !it.date.isAfter(today) }
                .sortedBy { it.date }
            val avgEnergy = if (period.isEmpty()) 0.0 else period.map { it.energy.toDouble() }.average()
            val avgFocus = if (period.isEmpty()) 0.0 else period.map { it.focus.toDouble() }.average()
            val avgUnderstanding = if (period.isEmpty()) 0.0 else period.map { it.understanding.toDouble() }.average()
            return PeriodStats(
                avgEnergy = avgEnergy,
                avgFocus = avgFocus,
                avgUnderstanding = avgUnderstanding,
                energyPoints = period.map { Pair(it.date, it.energy) },
                focusPoints = period.map { Pair(it.date, it.focus) },
            )
        }
    }
}

package uz.learningtracker.domain.usecase

import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.model.ReportItem
import uz.learningtracker.domain.model.WeeklyReport
import uz.learningtracker.domain.repository.DailyRepository
import java.time.LocalDate

class GetWeeklyReport(private val repository: DailyRepository) {

    suspend operator fun invoke(weekStart: LocalDate): WeeklyReport =
        compute(repository.getAll(), weekStart)

    companion object {
        fun compute(entries: List<DailyEntry>, weekStart: LocalDate): WeeklyReport {
            val weekEnd = weekStart.plusDays(6)
            val week = entries.filter { !it.date.isBefore(weekStart) && !it.date.isAfter(weekEnd) }

            val totalHours = week.sumOf { it.hoursWorked }
            val totalTasks = week.sumOf { it.tasksCompleted }
            val avgEnergy = if (week.isEmpty()) 0.0 else week.map { it.energy.toDouble() }.average()
            val avgFocus = if (week.isEmpty()) 0.0 else week.map { it.focus.toDouble() }.average()
            val avgUnderstanding = if (week.isEmpty()) 0.0 else week.map { it.understanding.toDouble() }.average()

            val topWins = week
                .filter { it.biggestWin.isNotBlank() }
                .sortedByDescending { it.energy + it.focus }
                .take(3)
                .map { ReportItem(it.date, it.biggestWin) }

            val topDifficulties = week
                .filter { it.biggestDifficulty.isNotBlank() }
                .sortedBy { it.energy + it.focus }
                .take(3)
                .map { ReportItem(it.date, it.biggestDifficulty) }

            val hoursPerWeekday = (0..6).map { offset ->
                week.firstOrNull { it.date == weekStart.plusDays(offset.toLong()) }?.hoursWorked ?: 0.0
            }

            return WeeklyReport(
                weekStart = weekStart,
                weekEnd = weekEnd,
                totalHours = totalHours,
                totalTasks = totalTasks,
                avgEnergy = avgEnergy,
                avgFocus = avgFocus,
                avgUnderstanding = avgUnderstanding,
                topWins = topWins,
                topDifficulties = topDifficulties,
                hoursPerWeekday = hoursPerWeekday,
            )
        }
    }
}

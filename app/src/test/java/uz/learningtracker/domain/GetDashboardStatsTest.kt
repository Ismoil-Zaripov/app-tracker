package uz.learningtracker.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.usecase.GetDashboardStats
import java.time.LocalDate

class GetDashboardStatsTest {

    private fun entry(date: LocalDate, energy: Int = 3, focus: Int = 4, understanding: Int = 4, hours: Double = 2.0) = DailyEntry(
        id = 0L,
        date = date,
        hoursWorked = hours,
        tasksCompleted = 3,
        mainTopic = "Kotlin",
        understanding = understanding,
        energy = energy,
        focus = focus,
        biggestWin = "",
        biggestDifficulty = "",
        createdAt = 1000L,
        updatedAt = 1000L,
    )

    // --- Streak tests ---

    @Test
    fun `streak is 0 when no entries`() {
        val stats = GetDashboardStats.compute(emptyList(), LocalDate.of(2024, 5, 24))
        assertEquals(0, stats.streak)
    }

    @Test
    fun `streak counts consecutive days ending today`() {
        val today = LocalDate.of(2024, 5, 24)
        val entries = listOf(
            entry(today),
            entry(today.minusDays(1)),
            entry(today.minusDays(2)),
        )
        assertEquals(3, GetDashboardStats.compute(entries, today).streak)
    }

    @Test
    fun `streak still alive when today missing but yesterday present`() {
        val today = LocalDate.of(2024, 5, 24)
        val entries = listOf(
            entry(today.minusDays(1)),
            entry(today.minusDays(2)),
            entry(today.minusDays(3)),
        )
        assertEquals(3, GetDashboardStats.compute(entries, today).streak)
    }

    @Test
    fun `streak is 0 when neither today nor yesterday has entry`() {
        val today = LocalDate.of(2024, 5, 24)
        val entries = listOf(entry(today.minusDays(2)))
        assertEquals(0, GetDashboardStats.compute(entries, today).streak)
    }

    @Test
    fun `streak counts single day`() {
        val today = LocalDate.of(2024, 5, 24)
        assertEquals(1, GetDashboardStats.compute(listOf(entry(today)), today).streak)
    }

    @Test
    fun `streak stops at gap in entries`() {
        val today = LocalDate.of(2024, 5, 24)
        val entries = listOf(
            entry(today),
            entry(today.minusDays(1)),
            // gap: minusDays(2) missing
            entry(today.minusDays(3)),
        )
        assertEquals(2, GetDashboardStats.compute(entries, today).streak)
    }

    // --- Average tests ---

    @Test
    fun `averages are 0 when no entries in period`() {
        val today = LocalDate.of(2024, 5, 24)
        val entries = listOf(entry(today.minusDays(10)))  // outside 7-day window
        val stats = GetDashboardStats.compute(entries, today)
        assertEquals(0.0, stats.period7.avgEnergy, 0.001)
        assertEquals(0.0, stats.period7.avgFocus, 0.001)
        assertEquals(0.0, stats.period7.avgUnderstanding, 0.001)
    }

    @Test
    fun `7-day averages include only last 7 days`() {
        val today = LocalDate.of(2024, 5, 24)
        val entries = listOf(
            entry(today, energy = 5, focus = 5, understanding = 5),
            entry(today.minusDays(6), energy = 3, focus = 3, understanding = 3),
            entry(today.minusDays(7), energy = 1, focus = 1, understanding = 1),  // excluded
        )
        val stats = GetDashboardStats.compute(entries, today)
        assertEquals(4.0, stats.period7.avgEnergy, 0.001)
        assertEquals(4.0, stats.period7.avgFocus, 0.001)
    }

    @Test
    fun `30-day averages include last 30 days`() {
        val today = LocalDate.of(2024, 5, 24)
        val entries = listOf(
            entry(today, energy = 4, focus = 4, understanding = 4),
            entry(today.minusDays(29), energy = 2, focus = 2, understanding = 2),
            entry(today.minusDays(30), energy = 1, focus = 1, understanding = 1),  // excluded
        )
        val stats = GetDashboardStats.compute(entries, today)
        assertEquals(3.0, stats.period30.avgEnergy, 0.001)
        assertEquals(3.0, stats.period30.avgFocus, 0.001)
    }

    @Test
    fun `hours this week only includes monday to today`() {
        val today = LocalDate.of(2024, 5, 24)  // Friday
        val entries = listOf(
            entry(today, hours = 2.0),
            entry(today.minusDays(1), hours = 3.0),  // Thursday — in week
            entry(today.minusDays(5), hours = 5.0),  // Sunday — previous week, excluded
        )
        val stats = GetDashboardStats.compute(entries, today)
        assertEquals(5.0, stats.hoursThisWeek, 0.001)
    }

    @Test
    fun `hours this month only includes current month`() {
        val today = LocalDate.of(2024, 5, 24)
        val entries = listOf(
            entry(LocalDate.of(2024, 5, 1), hours = 4.0),
            entry(LocalDate.of(2024, 5, 24), hours = 3.0),
            entry(LocalDate.of(2024, 4, 30), hours = 10.0),  // last month, excluded
        )
        val stats = GetDashboardStats.compute(entries, today)
        assertEquals(7.0, stats.hoursThisMonth, 0.001)
    }

    @Test
    fun `hasTodayEntry false when no entry today`() {
        val today = LocalDate.of(2024, 5, 24)
        val stats = GetDashboardStats.compute(listOf(entry(today.minusDays(1))), today)
        assertEquals(false, stats.hasTodayEntry)
    }

    @Test
    fun `hasTodayEntry true when entry exists today`() {
        val today = LocalDate.of(2024, 5, 24)
        val stats = GetDashboardStats.compute(listOf(entry(today)), today)
        assertEquals(true, stats.hasTodayEntry)
    }
}

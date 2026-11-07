package uz.learningtracker.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.usecase.GetWeeklyReport
import java.time.LocalDate

class GetWeeklyReportTest {

    private val monday = LocalDate.of(2024, 5, 20) // Mon 20 May 2024

    private fun entry(
        date: LocalDate,
        hours: Double = 2.0,
        tasks: Int = 3,
        energy: Int = 3,
        focus: Int = 3,
        understanding: Int = 3,
        win: String = "",
        difficulty: String = "",
    ) = DailyEntry(
        id = 0L,
        date = date,
        hoursWorked = hours,
        tasksCompleted = tasks,
        mainTopic = "Kotlin",
        understanding = understanding,
        energy = energy,
        focus = focus,
        biggestWin = win,
        biggestDifficulty = difficulty,
        createdAt = 1000L,
        updatedAt = 1000L,
    )

    @Test
    fun `empty week returns zeroed report`() {
        val report = GetWeeklyReport.compute(emptyList(), monday)
        assertEquals(0.0, report.totalHours, 0.001)
        assertEquals(0, report.totalTasks)
        assertEquals(0.0, report.avgEnergy, 0.001)
        assertEquals(0.0, report.avgFocus, 0.001)
        assertEquals(0.0, report.avgUnderstanding, 0.001)
        assertTrue(report.topWins.isEmpty())
        assertTrue(report.topDifficulties.isEmpty())
        assertEquals(7, report.hoursPerWeekday.size)
        assertTrue(report.hoursPerWeekday.all { it == 0.0 })
    }

    @Test
    fun `only entries within Mon-Sun are included`() {
        val entries = listOf(
            entry(monday.minusDays(1), hours = 10.0), // Sunday before — excluded
            entry(monday, hours = 2.0),
            entry(monday.plusDays(6), hours = 3.0),   // Sunday of week — included
            entry(monday.plusDays(7), hours = 10.0),  // Monday after — excluded
        )
        val report = GetWeeklyReport.compute(entries, monday)
        assertEquals(5.0, report.totalHours, 0.001)
    }

    @Test
    fun `partial week sums hours and tasks correctly`() {
        val entries = listOf(
            entry(monday, hours = 1.5, tasks = 2),
            entry(monday.plusDays(2), hours = 3.0, tasks = 5),
        )
        val report = GetWeeklyReport.compute(entries, monday)
        assertEquals(4.5, report.totalHours, 0.001)
        assertEquals(7, report.totalTasks)
    }

    @Test
    fun `averages computed over week entries only`() {
        val entries = listOf(
            entry(monday, energy = 5, focus = 4, understanding = 3),
            entry(monday.plusDays(1), energy = 3, focus = 2, understanding = 5),
        )
        val report = GetWeeklyReport.compute(entries, monday)
        assertEquals(4.0, report.avgEnergy, 0.001)
        assertEquals(3.0, report.avgFocus, 0.001)
        assertEquals(4.0, report.avgUnderstanding, 0.001)
    }

    @Test
    fun `top wins are top 3 non-blank wins by highest energy+focus`() {
        val entries = listOf(
            entry(monday, energy = 5, focus = 5, win = "best"),
            entry(monday.plusDays(1), energy = 4, focus = 4, win = "second"),
            entry(monday.plusDays(2), energy = 3, focus = 3, win = "third"),
            entry(monday.plusDays(3), energy = 2, focus = 2, win = "fourth"),
            entry(monday.plusDays(4), energy = 1, focus = 1, win = ""),    // blank, excluded
        )
        val report = GetWeeklyReport.compute(entries, monday)
        assertEquals(3, report.topWins.size)
        assertEquals("best", report.topWins[0].text)
        assertEquals("second", report.topWins[1].text)
        assertEquals("third", report.topWins[2].text)
    }

    @Test
    fun `top difficulties are top 3 non-blank by lowest energy+focus`() {
        val entries = listOf(
            entry(monday, energy = 1, focus = 1, difficulty = "hardest"),
            entry(monday.plusDays(1), energy = 2, focus = 2, difficulty = "second"),
            entry(monday.plusDays(2), energy = 3, focus = 3, difficulty = "third"),
            entry(monday.plusDays(3), energy = 5, focus = 5, difficulty = "easy"),
            entry(monday.plusDays(4), difficulty = ""),                    // blank, excluded
        )
        val report = GetWeeklyReport.compute(entries, monday)
        assertEquals(3, report.topDifficulties.size)
        assertEquals("hardest", report.topDifficulties[0].text)
        assertEquals("second", report.topDifficulties[1].text)
        assertEquals("third", report.topDifficulties[2].text)
    }

    @Test
    fun `blank wins and difficulties are skipped`() {
        val entries = listOf(
            entry(monday, win = "", difficulty = ""),
            entry(monday.plusDays(1), win = "  ", difficulty = "  "),
        )
        val report = GetWeeklyReport.compute(entries, monday)
        assertTrue(report.topWins.isEmpty())
        assertTrue(report.topDifficulties.isEmpty())
    }

    @Test
    fun `hoursPerWeekday has 7 elements aligned Mon to Sun`() {
        val entries = listOf(
            entry(monday, hours = 1.0),               // Monday index 0
            entry(monday.plusDays(2), hours = 3.0),   // Wednesday index 2
            entry(monday.plusDays(6), hours = 2.0),   // Sunday index 6
        )
        val report = GetWeeklyReport.compute(entries, monday)
        assertEquals(7, report.hoursPerWeekday.size)
        assertEquals(1.0, report.hoursPerWeekday[0], 0.001)
        assertEquals(0.0, report.hoursPerWeekday[1], 0.001)
        assertEquals(3.0, report.hoursPerWeekday[2], 0.001)
        assertEquals(2.0, report.hoursPerWeekday[6], 0.001)
    }

    @Test
    fun `weekEnd is always 6 days after weekStart`() {
        val report = GetWeeklyReport.compute(emptyList(), monday)
        assertEquals(monday.plusDays(6), report.weekEnd)
    }

    @Test
    fun `week boundary is Mon-Sun inclusive`() {
        val entries = listOf(
            entry(monday, hours = 1.0),
            entry(monday.plusDays(6), hours = 1.0),
        )
        val report = GetWeeklyReport.compute(entries, monday)
        assertEquals(2.0, report.totalHours, 0.001)
    }
}

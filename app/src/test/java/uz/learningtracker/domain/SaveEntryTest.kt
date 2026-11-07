package uz.learningtracker.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.repository.DailyRepository
import uz.learningtracker.domain.usecase.SaveEntry
import java.time.LocalDate

class SaveEntryTest {

    private lateinit var repository: FakeDailyRepository
    private lateinit var saveEntry: SaveEntry

    @Before
    fun setUp() {
        repository = FakeDailyRepository()
        saveEntry = SaveEntry(repository)
    }

    @Test
    fun `saving new entry stores it in repository`() = runTest {
        val entry = entry(date = LocalDate.of(2024, 5, 24))
        saveEntry(entry)
        assertEquals(entry, repository.saved.last())
    }

    @Test
    fun `saving entry for existing date replaces it`() = runTest {
        val date = LocalDate.of(2024, 5, 24)
        val original = entry(id = 1L, date = date, mainTopic = "First")
        val updated = entry(id = 1L, date = date, mainTopic = "Updated")

        saveEntry(original)
        saveEntry(updated)

        assertEquals(2, repository.saved.size)
        assertEquals("Updated", repository.saved.last().mainTopic)
    }

    @Test
    fun `saving two different dates creates two entries`() = runTest {
        saveEntry(entry(date = LocalDate.of(2024, 5, 24)))
        saveEntry(entry(date = LocalDate.of(2024, 5, 25)))
        assertEquals(2, repository.saved.size)
    }

    private fun entry(
        id: Long = 0L,
        date: LocalDate = LocalDate.of(2024, 5, 24),
        mainTopic: String = "Kotlin",
    ) = DailyEntry(
        id = id,
        date = date,
        hoursWorked = 3.0,
        tasksCompleted = 5,
        mainTopic = mainTopic,
        understanding = 4,
        energy = 3,
        focus = 4,
        biggestWin = "Done",
        biggestDifficulty = "Hard",
        createdAt = 1000L,
        updatedAt = 1000L,
    )
}

private class FakeDailyRepository : DailyRepository {
    val saved = mutableListOf<DailyEntry>()

    override suspend fun save(entry: DailyEntry) { saved.add(entry) }
    override suspend fun getByDate(date: LocalDate): DailyEntry? = saved.lastOrNull { it.date == date }
    override suspend fun getAll(): List<DailyEntry> = saved.toList()
    override fun observeAll(): Flow<List<DailyEntry>> = flowOf(saved.toList())
    override suspend fun delete(entry: DailyEntry) { saved.removeIf { it.id == entry.id } }
}

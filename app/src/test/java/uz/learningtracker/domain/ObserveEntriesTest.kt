package uz.learningtracker.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.repository.DailyRepository
import uz.learningtracker.domain.usecase.DeleteEntry
import uz.learningtracker.domain.usecase.ObserveEntries
import java.time.LocalDate

class ObserveEntriesTest {

    private lateinit var repository: FlowFakeRepository
    private lateinit var observeEntries: ObserveEntries

    @Before
    fun setUp() {
        repository = FlowFakeRepository()
        observeEntries = ObserveEntries(repository)
    }

    @Test
    fun `no filter returns all entries`() = runTest {
        repository.setEntries(
            entry(LocalDate.of(2024, 5, 20)),
            entry(LocalDate.of(2024, 5, 21)),
            entry(LocalDate.of(2024, 5, 22)),
        )
        val result = observeEntries().first()
        assertEquals(3, result.size)
    }

    @Test
    fun `filter by from date excludes earlier entries`() = runTest {
        repository.setEntries(
            entry(LocalDate.of(2024, 5, 18)),
            entry(LocalDate.of(2024, 5, 20)),
            entry(LocalDate.of(2024, 5, 22)),
        )
        val result = observeEntries(from = LocalDate.of(2024, 5, 20)).first()
        assertEquals(2, result.size)
        assertEquals(LocalDate.of(2024, 5, 20), result[0].date)
        assertEquals(LocalDate.of(2024, 5, 22), result[1].date)
    }

    @Test
    fun `filter by to date excludes later entries`() = runTest {
        repository.setEntries(
            entry(LocalDate.of(2024, 5, 18)),
            entry(LocalDate.of(2024, 5, 20)),
            entry(LocalDate.of(2024, 5, 22)),
        )
        val result = observeEntries(to = LocalDate.of(2024, 5, 20)).first()
        assertEquals(2, result.size)
        assertEquals(LocalDate.of(2024, 5, 18), result[0].date)
        assertEquals(LocalDate.of(2024, 5, 20), result[1].date)
    }

    @Test
    fun `filter by range keeps only matching entries`() = runTest {
        repository.setEntries(
            entry(LocalDate.of(2024, 5, 17)),
            entry(LocalDate.of(2024, 5, 18)),
            entry(LocalDate.of(2024, 5, 20)),
            entry(LocalDate.of(2024, 5, 23)),
        )
        val result = observeEntries(
            from = LocalDate.of(2024, 5, 18),
            to = LocalDate.of(2024, 5, 20),
        ).first()
        assertEquals(2, result.size)
    }

    @Test
    fun `filter range with no matches returns empty`() = runTest {
        repository.setEntries(
            entry(LocalDate.of(2024, 5, 10)),
            entry(LocalDate.of(2024, 5, 11)),
        )
        val result = observeEntries(
            from = LocalDate.of(2024, 5, 20),
            to = LocalDate.of(2024, 5, 25),
        ).first()
        assertEquals(0, result.size)
    }

    @Test
    fun `empty repository returns empty list`() = runTest {
        val result = observeEntries().first()
        assertEquals(0, result.size)
    }
}

class DeleteEntryTest {

    private lateinit var repository: FlowFakeRepository
    private lateinit var deleteEntry: DeleteEntry

    @Before
    fun setUp() {
        repository = FlowFakeRepository()
        deleteEntry = DeleteEntry(repository)
    }

    @Test
    fun `delete removes entry from repository`() = runTest {
        val e = entry(LocalDate.of(2024, 5, 20), id = 1L)
        repository.setEntries(e)
        deleteEntry(e)
        assertEquals(0, repository.observeAll().first().size)
    }

    @Test
    fun `delete removes only the target entry`() = runTest {
        val e1 = entry(LocalDate.of(2024, 5, 20), id = 1L)
        val e2 = entry(LocalDate.of(2024, 5, 21), id = 2L)
        repository.setEntries(e1, e2)
        deleteEntry(e1)
        val remaining = repository.observeAll().first()
        assertEquals(1, remaining.size)
        assertEquals(e2.id, remaining[0].id)
    }
}

private fun entry(date: LocalDate, id: Long = 0L) = DailyEntry(
    id = id,
    date = date,
    hoursWorked = 2.0,
    tasksCompleted = 3,
    mainTopic = "Kotlin",
    understanding = 4,
    energy = 3,
    focus = 4,
    biggestWin = "",
    biggestDifficulty = "",
    createdAt = 1000L,
    updatedAt = 1000L,
)

private class FlowFakeRepository : DailyRepository {
    private val flow = MutableStateFlow<List<DailyEntry>>(emptyList())

    fun setEntries(vararg entries: DailyEntry) { flow.value = entries.toList() }

    override suspend fun save(entry: DailyEntry) { flow.value = flow.value + entry }
    override suspend fun getByDate(date: LocalDate) = flow.value.lastOrNull { it.date == date }
    override suspend fun getAll() = flow.value
    override fun observeAll(): Flow<List<DailyEntry>> = flow
    override suspend fun delete(entry: DailyEntry) {
        flow.value = flow.value.filter { it.id != entry.id }
    }
}

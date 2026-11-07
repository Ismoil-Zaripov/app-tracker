package uz.learningtracker.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.learningtracker.domain.model.DailyEntry
import java.time.LocalDate

interface DailyRepository {
    suspend fun save(entry: DailyEntry)
    suspend fun getByDate(date: LocalDate): DailyEntry?
    suspend fun getAll(): List<DailyEntry>
    fun observeAll(): Flow<List<DailyEntry>>
    suspend fun delete(entry: DailyEntry)
}

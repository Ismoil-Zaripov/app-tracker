package uz.learningtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.learningtracker.data.db.DailyEntryDao
import uz.learningtracker.data.mapper.toDomain
import uz.learningtracker.data.mapper.toEntity
import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.repository.DailyRepository
import java.time.LocalDate

class DailyRepositoryImpl(private val dao: DailyEntryDao) : DailyRepository {
    override suspend fun save(entry: DailyEntry) = dao.upsert(entry.toEntity())
    override suspend fun getByDate(date: LocalDate) = dao.getByDate(date.toEpochDay())?.toDomain()
    override suspend fun getAll() = dao.getAll().map { it.toDomain() }
    override fun observeAll(): Flow<List<DailyEntry>> = dao.observeAll().map { list -> list.map { it.toDomain() } }
    override suspend fun delete(entry: DailyEntry) = dao.delete(entry.toEntity())
}

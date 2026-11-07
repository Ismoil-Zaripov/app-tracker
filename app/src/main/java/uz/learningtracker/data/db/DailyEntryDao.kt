package uz.learningtracker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyEntryDao {
    @Upsert
    suspend fun upsert(entry: DailyEntryEntity)

    @Query("SELECT * FROM daily_entries WHERE date = :epochDay LIMIT 1")
    suspend fun getByDate(epochDay: Long): DailyEntryEntity?

    @Query("SELECT * FROM daily_entries ORDER BY date DESC")
    suspend fun getAll(): List<DailyEntryEntity>

    @Query("SELECT * FROM daily_entries ORDER BY date DESC")
    fun observeAll(): Flow<List<DailyEntryEntity>>

    @Delete
    suspend fun delete(entry: DailyEntryEntity)
}

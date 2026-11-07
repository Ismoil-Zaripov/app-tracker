package uz.learningtracker.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_entries",
    indices = [Index(value = ["date"], unique = true)],
)
data class DailyEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val hoursWorked: Double,
    val tasksCompleted: Int,
    val mainTopic: String,
    val understanding: Int,
    val energy: Int,
    val focus: Int,
    val biggestWin: String,
    val biggestDifficulty: String,
    val createdAt: Long,
    val updatedAt: Long,
)

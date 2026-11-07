package uz.learningtracker.data.mapper

import uz.learningtracker.data.db.DailyEntryEntity
import uz.learningtracker.domain.model.DailyEntry
import java.time.LocalDate

fun DailyEntryEntity.toDomain() = DailyEntry(
    id = id,
    date = LocalDate.ofEpochDay(date),
    hoursWorked = hoursWorked,
    tasksCompleted = tasksCompleted,
    mainTopic = mainTopic,
    understanding = understanding,
    energy = energy,
    focus = focus,
    biggestWin = biggestWin,
    biggestDifficulty = biggestDifficulty,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun DailyEntry.toEntity() = DailyEntryEntity(
    id = id,
    date = date.toEpochDay(),
    hoursWorked = hoursWorked,
    tasksCompleted = tasksCompleted,
    mainTopic = mainTopic,
    understanding = understanding,
    energy = energy,
    focus = focus,
    biggestWin = biggestWin,
    biggestDifficulty = biggestDifficulty,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

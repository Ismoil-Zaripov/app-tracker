package uz.learningtracker.domain.usecase

import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.repository.DailyRepository

class SaveEntry(private val repository: DailyRepository) {
    suspend operator fun invoke(entry: DailyEntry) = repository.save(entry)
}

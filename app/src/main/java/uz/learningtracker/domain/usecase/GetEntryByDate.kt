package uz.learningtracker.domain.usecase

import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.repository.DailyRepository
import java.time.LocalDate

class GetEntryByDate(private val repository: DailyRepository) {
    suspend operator fun invoke(date: LocalDate): DailyEntry? = repository.getByDate(date)
}

package uz.learningtracker.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.domain.repository.DailyRepository
import java.time.LocalDate

class ObserveEntries(private val repository: DailyRepository) {
    operator fun invoke(from: LocalDate? = null, to: LocalDate? = null): Flow<List<DailyEntry>> =
        repository.observeAll().map { entries ->
            if (from == null && to == null) entries
            else entries.filter { entry ->
                (from == null || !entry.date.isBefore(from)) &&
                        (to == null || !entry.date.isAfter(to))
            }
        }
}

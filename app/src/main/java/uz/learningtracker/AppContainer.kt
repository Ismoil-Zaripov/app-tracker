package uz.learningtracker

import android.content.Context
import uz.learningtracker.data.ThemePreferencesRepository
import uz.learningtracker.data.db.AppDatabase
import uz.learningtracker.data.repository.DailyRepositoryImpl
import uz.learningtracker.domain.repository.DailyRepository

class AppContainer(context: Context) {
    val themeRepo = ThemePreferencesRepository(context)

    private val database = AppDatabase.create(context)
    val dailyRepository: DailyRepository = DailyRepositoryImpl(database.dailyEntryDao())
}

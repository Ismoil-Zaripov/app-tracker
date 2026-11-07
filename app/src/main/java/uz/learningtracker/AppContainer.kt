package uz.learningtracker

import android.content.Context
import uz.learningtracker.data.ThemePreferencesRepository
import uz.learningtracker.data.db.AppDatabase
import uz.learningtracker.data.repository.DailyRepositoryImpl
import uz.learningtracker.domain.repository.DailyRepository
import uz.learningtracker.domain.usecase.DeleteEntry
import uz.learningtracker.domain.usecase.GetDashboardStats
import uz.learningtracker.domain.usecase.GetEntryByDate
import uz.learningtracker.domain.usecase.ObserveEntries
import uz.learningtracker.domain.usecase.GetWeeklyReport
import uz.learningtracker.domain.usecase.SaveEntry

class AppContainer(context: Context) {
    val themeRepo = ThemePreferencesRepository(context)

    private val database = AppDatabase.create(context)
    val dailyRepository: DailyRepository = DailyRepositoryImpl(database.dailyEntryDao())

    val saveEntry = SaveEntry(dailyRepository)
    val getEntryByDate = GetEntryByDate(dailyRepository)
    val observeEntries = ObserveEntries(dailyRepository)
    val deleteEntry = DeleteEntry(dailyRepository)
    val getDashboardStats = GetDashboardStats(dailyRepository)
    val getWeeklyReport = GetWeeklyReport(dailyRepository)
}

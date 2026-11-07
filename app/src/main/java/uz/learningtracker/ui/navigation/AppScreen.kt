package uz.learningtracker.ui.navigation

sealed class AppScreen(val route: String) {
    object Bugun : AppScreen("bugun?date={date}") {
        const val BASE = "bugun"
        fun withDate(date: String) = "bugun?date=$date"
    }
    object Dashboard : AppScreen("dashboard")
    object Tarix : AppScreen("tarix")
    object Hisobot : AppScreen("hisobot")
}

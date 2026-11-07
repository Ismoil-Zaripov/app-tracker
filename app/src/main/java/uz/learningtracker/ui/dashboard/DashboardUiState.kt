package uz.learningtracker.ui.dashboard

import uz.learningtracker.domain.model.DashboardStats

data class DashboardUiState(
    val stats: DashboardStats? = null,
    val selectedPeriod: Int = 7,
)

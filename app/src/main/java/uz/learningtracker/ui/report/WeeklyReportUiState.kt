package uz.learningtracker.ui.report

import uz.learningtracker.domain.model.WeeklyReport
import java.time.LocalDate

data class WeeklyReportUiState(
    val weekStart: LocalDate,
    val report: WeeklyReport? = null,
)

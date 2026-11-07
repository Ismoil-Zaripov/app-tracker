package uz.learningtracker.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.learningtracker.R
import uz.learningtracker.domain.model.ReportItem
import uz.learningtracker.domain.model.WeeklyReport
import uz.learningtracker.ui.components.EmptyState
import uz.learningtracker.ui.components.SectionCard
import uz.learningtracker.ui.components.StatCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HisobotScreen(
    viewModel: WeeklyReportViewModel,
    onAddEntry: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val months = stringArrayResource(R.array.month_names_uz)
    val weekDays = stringArrayResource(R.array.week_day_abbrs)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        WeekSelector(
            weekStart = state.weekStart,
            weekEnd = state.weekStart.plusDays(6),
            onPrevious = viewModel::previousWeek,
            onNext = viewModel::nextWeek,
        )

        val report = state.report
        val isEmpty = report == null || report.totalHours == 0.0 && report.totalTasks == 0

        if (isEmpty) {
            EmptyWeekContent(
                weekStart = state.weekStart,
                weekEnd = state.weekStart.plusDays(6),
                months = months,
                onAddEntry = onAddEntry,
                onGoToCurrent = viewModel::goToCurrentWeek,
            )
        } else {
            ReportContent(
                report = report!!,
                weekDays = weekDays,
            )
        }
    }
}

@Composable
private fun EmptyWeekContent(
    weekStart: LocalDate,
    weekEnd: LocalDate,
    months: Array<String>,
    onAddEntry: () -> Unit,
    onGoToCurrent: () -> Unit,
) {
    EmptyState(
        icon = Icons.Outlined.CalendarMonth,
        title = stringResource(R.string.hisobot_empty_title),
        subtitle = stringResource(R.string.hisobot_empty_subtitle),
        modifier = Modifier.fillMaxWidth(),
        action = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onAddEntry,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.hisobot_add_entry))
                }
                OutlinedButton(
                    onClick = onGoToCurrent,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.hisobot_go_to_current))
                }
            }
        },
    )
}

@Composable
private fun ReportContent(
    report: WeeklyReport,
    weekDays: Array<String>,
) {
    // Summary cards
    SectionCard(title = stringResource(R.string.hisobot_summary_title)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatCard(
                label = stringResource(R.string.hisobot_total_hours),
                value = stringResource(R.string.hisobot_hours_unit, formatHours(report.totalHours)),
                modifier = Modifier.weight(1f),
            )
            StatCard(
                label = stringResource(R.string.hisobot_total_tasks),
                value = stringResource(R.string.hisobot_tasks_unit, report.totalTasks),
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StatCard(
                label = stringResource(R.string.hisobot_avg_energy),
                value = "${formatRating(report.avgEnergy)} ${stringResource(R.string.hisobot_out_of_5)}",
                modifier = Modifier.weight(1f),
            )
            StatCard(
                label = stringResource(R.string.hisobot_avg_focus),
                value = "${formatRating(report.avgFocus)} ${stringResource(R.string.hisobot_out_of_5)}",
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(12.dp))
        StatCard(
            label = stringResource(R.string.hisobot_avg_understanding),
            value = "${formatRating(report.avgUnderstanding)} ${stringResource(R.string.hisobot_out_of_5)}",
            modifier = Modifier.fillMaxWidth(),
        )
    }

    // Bar chart
    SectionCard(title = stringResource(R.string.hisobot_chart_title)) {
        val avgHours = if (report.hoursPerWeekday.isEmpty()) 0.0
        else report.hoursPerWeekday.average()
        Text(
            text = stringResource(R.string.hisobot_chart_avg, formatHours(avgHours)),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        BarChart(
            values = report.hoursPerWeekday,
            labels = weekDays.take(7),
            barColor = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            labelStyle = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }

    // Top wins
    if (report.topWins.isNotEmpty()) {
        SectionCard(title = stringResource(R.string.hisobot_wins_title)) {
            report.topWins.forEachIndexed { i, item ->
                if (i > 0) Spacer(Modifier.height(10.dp))
                ReportItemRow(item = item)
            }
        }
    }

    // Top difficulties
    if (report.topDifficulties.isNotEmpty()) {
        SectionCard(title = stringResource(R.string.hisobot_difficulties_title)) {
            report.topDifficulties.forEachIndexed { i, item ->
                if (i > 0) Spacer(Modifier.height(10.dp))
                ReportItemRow(item = item)
            }
        }
    }
}

@Composable
private fun ReportItemRow(item: ReportItem) {
    val monthFormatter = DateTimeFormatter.ofPattern("MMM d")
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = item.date.format(monthFormatter),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
            )
        }
    }
}

private fun formatHours(hours: Double): String =
    if (hours == hours.toLong().toDouble()) hours.toLong().toString()
    else "%.1f".format(hours)

private fun formatRating(value: Double): String = "%.1f".format(value)

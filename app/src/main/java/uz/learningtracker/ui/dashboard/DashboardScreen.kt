package uz.learningtracker.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.learningtracker.R
import uz.learningtracker.domain.model.DashboardStats
import uz.learningtracker.ui.components.EmptyState
import uz.learningtracker.ui.components.SectionCard
import uz.learningtracker.ui.components.StatCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddEntry: () -> Unit,
    onEditToday: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val stats = state.stats
    if (stats == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {}
        return
    }

    if (stats.period7.energyPoints.isEmpty() && !stats.hasTodayEntry && stats.streak == 0) {
        EmptyState(
            icon = Icons.Outlined.Insights,
            title = stringResource(R.string.dashboard_no_data_title),
            subtitle = stringResource(R.string.dashboard_no_data_subtitle),
            modifier = Modifier.fillMaxSize(),
            action = {
                Button(onClick = onAddEntry) {
                    Text(stringResource(R.string.dashboard_add_entry))
                }
            },
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        StreakCard(streak = stats.streak)

        TodayStatusCard(
            stats = stats,
            onAddEntry = onAddEntry,
            onEditToday = onEditToday,
        )

        AveragesCard(
            stats = stats,
            selectedPeriod = state.selectedPeriod,
            onSelectPeriod = viewModel::selectPeriod,
        )

        HoursRow(stats = stats)

        ChartCard(
            stats = stats,
            selectedPeriod = state.selectedPeriod,
        )
    }
}

@Composable
private fun StreakCard(streak: Int) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colorScheme.primary.copy(alpha = 0.15f),
                            colorScheme.primaryContainer.copy(alpha = 0f),
                        ),
                        center = Offset(0f, 0f),
                        radius = 600f,
                    ),
                )
                .padding(20.dp),
        ) {
            Column {
                Text(
                    text = stringResource(R.string.dashboard_streak_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = colorScheme.onPrimaryContainer,
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = streak.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onPrimaryContainer,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.dashboard_streak_unit),
                        style = MaterialTheme.typography.headlineMedium,
                        color = colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayStatusCard(
    stats: DashboardStats,
    onAddEntry: () -> Unit,
    onEditToday: () -> Unit,
) {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("d-MMM, yyyy", Locale.forLanguageTag("uz"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.dashboard_today_section),
                    style = MaterialTheme.typography.titleMedium,
                )
                if (stats.hasTodayEntry) {
                    AssistChip(
                        onClick = {},
                        label = { Text(stringResource(R.string.dashboard_today_done), style = MaterialTheme.typography.labelMedium) },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                        border = null,
                    )
                } else {
                    AssistChip(
                        onClick = {},
                        label = { Text(stringResource(R.string.dashboard_today_not_entered), style = MaterialTheme.typography.labelMedium) },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            labelColor = MaterialTheme.colorScheme.onErrorContainer,
                            leadingIconContentColor = MaterialTheme.colorScheme.onErrorContainer,
                        ),
                        border = null,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = today.format(formatter),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (stats.hasTodayEntry) {
                val entry = stats.todayEntry!!
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val hoursStr = entry.hoursWorked.let {
                        if (it == it.toLong().toDouble()) it.toLong().toString() else it.toString()
                    }
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                stringResource(R.string.dashboard_hours_format, hoursStr),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        border = null,
                    )
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                "${entry.tasksCompleted} ta vazifa",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        border = null,
                    )
                    Spacer(Modifier.weight(1f))
                    OutlinedButton(
                        onClick = onEditToday,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp),
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = stringResource(R.string.dashboard_edit_today_cd), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.dashboard_edit_today), style = MaterialTheme.typography.labelSmall)
                    }
                }
            } else {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.dashboard_today_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onAddEntry,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraSmall,
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.dashboard_add_entry))
                }
            }
        }
    }
}

@Composable
private fun AveragesCard(
    stats: DashboardStats,
    selectedPeriod: Int,
    onSelectPeriod: (Int) -> Unit,
) {
    val period = if (selectedPeriod == 7) stats.period7 else stats.period30
    val colorScheme = MaterialTheme.colorScheme

    SectionCard(
        title = stringResource(R.string.dashboard_averages_title),
        action = {
            PeriodToggle(selectedPeriod = selectedPeriod, onSelect = onSelectPeriod)
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            MetricRingItem(
                label = stringResource(R.string.dashboard_energy_label),
                value = period.avgEnergy,
                color = colorScheme.tertiary,
                trackColor = colorScheme.surfaceContainerHighest,
            )
            MetricRingItem(
                label = stringResource(R.string.dashboard_focus_label),
                value = period.avgFocus,
                color = colorScheme.primary,
                trackColor = colorScheme.surfaceContainerHighest,
            )
            MetricRingItem(
                label = stringResource(R.string.dashboard_understanding_label),
                value = period.avgUnderstanding,
                color = colorScheme.secondary,
                trackColor = colorScheme.surfaceContainerHighest,
            )
        }
    }
}

@Composable
private fun PeriodToggle(selectedPeriod: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    ) {
        PeriodToggleItem(
            label = stringResource(R.string.dashboard_7days),
            selected = selectedPeriod == 7,
            onClick = { onSelect(7) },
        )
        PeriodToggleItem(
            label = stringResource(R.string.dashboard_30days),
            selected = selectedPeriod == 30,
            onClick = { onSelect(30) },
        )
    }
}

@Composable
private fun PeriodToggleItem(label: String, selected: Boolean, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(if (selected) colorScheme.secondaryContainer else colorScheme.surfaceContainerHighest)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = colorScheme.onSecondaryContainer,
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) colorScheme.onSecondaryContainer else colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MetricRingItem(
    label: String,
    value: Double,
    color: androidx.compose.ui.graphics.Color,
    trackColor: androidx.compose.ui.graphics.Color,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
            Canvas(modifier = Modifier.size(72.dp)) {
                val strokeWidth = 6.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2f
                val center = Offset(size.width / 2f, size.height / 2f)
                val sweep = (value.toFloat().coerceIn(0f, 5f) / 5f) * 270f
                val arcTopLeft = Offset(center.x - radius, center.y - radius)
                val arcSize = androidx.compose.ui.geometry.Size(radius * 2f, radius * 2f)
                drawArc(
                    color = trackColor,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round),
                    topLeft = arcTopLeft,
                    size = arcSize,
                )
                drawArc(
                    color = color,
                    startAngle = 135f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round),
                    topLeft = arcTopLeft,
                    size = arcSize,
                )
            }
            val formatted = if (value == 0.0) "-" else String.format(Locale.US, "%.1f", value)
            Text(
                text = formatted,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.dashboard_out_of_5),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun HoursRow(stats: DashboardStats) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val weekHours = stats.hoursThisWeek.let {
            if (it == it.toLong().toDouble()) it.toLong().toString() else String.format(Locale.US, "%.1f", it)
        }
        val monthHours = stats.hoursThisMonth.let {
            if (it == it.toLong().toDouble()) it.toLong().toString() else String.format(Locale.US, "%.1f", it)
        }
        StatCard(
            label = stringResource(R.string.dashboard_this_week),
            value = stringResource(R.string.dashboard_hours_format, weekHours),
            modifier = Modifier.weight(1f),
        )
        StatCard(
            label = stringResource(R.string.dashboard_this_month),
            value = stringResource(R.string.dashboard_hours_format, monthHours),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ChartCard(stats: DashboardStats, selectedPeriod: Int) {
    val period = if (selectedPeriod == 7) stats.period7 else stats.period30
    val today = LocalDate.now()
    val dayAbbrs = stringArrayResource(R.array.week_day_abbrs)
    val todayLabel = stringResource(R.string.dashboard_today_label)
    val subtitle = if (selectedPeriod == 7) {
        stringResource(R.string.dashboard_chart_subtitle_7)
    } else {
        stringResource(R.string.dashboard_chart_subtitle_30)
    }

    val days = selectedPeriod
    val dates = (days - 1 downTo 0).map { today.minusDays(it.toLong()) }
    val energyMap = period.energyPoints.associate { it.first to it.second.toFloat() }
    val focusMap = period.focusPoints.associate { it.first to it.second.toFloat() }
    val energyValues = dates.map { energyMap[it] }
    val focusValues = dates.map { focusMap[it] }
    val labels = dates.map { date ->
        if (date == today && !stats.hasTodayEntry) todayLabel
        else dayAbbrs[date.dayOfWeek.value - 1]
    }

    SectionCard(title = stringResource(R.string.dashboard_chart_title)) {
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 8.dp),
        ) {
            LegendDot(
                color = MaterialTheme.colorScheme.primary,
                label = stringResource(R.string.dashboard_focus_label),
            )
            LegendDot(
                color = MaterialTheme.colorScheme.tertiary,
                label = stringResource(R.string.dashboard_energy_label),
            )
        }
        LineChart(
            energyPoints = energyValues,
            focusPoints = focusValues,
            xLabels = labels,
        )
    }
}

@Composable
private fun LegendDot(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(color),
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

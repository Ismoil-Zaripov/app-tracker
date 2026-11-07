package uz.learningtracker.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.learningtracker.R
import java.time.LocalDate

@Composable
fun WeekSelector(
    weekStart: LocalDate,
    weekEnd: LocalDate,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val months = stringArrayResource(R.array.month_names_uz)
    val label = formatWeekRange(weekStart, weekEnd, months)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onPrevious, modifier = Modifier.size(48.dp)) {
            Icon(
                imageVector = Icons.Outlined.ChevronLeft,
                contentDescription = stringResource(R.string.hisobot_prev_week_cd),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onNext, modifier = Modifier.size(48.dp)) {
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = stringResource(R.string.hisobot_next_week_cd),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

fun formatWeekRange(start: LocalDate, end: LocalDate, monthNames: Array<String>): String {
    val startMonth = monthNames[start.monthValue - 1]
    val endMonth = monthNames[end.monthValue - 1]
    return if (start.month == end.month && start.year == end.year) {
        "${start.dayOfMonth} – ${end.dayOfMonth} $startMonth, ${start.year}"
    } else if (start.year == end.year) {
        "${start.dayOfMonth} $startMonth – ${end.dayOfMonth} $endMonth, ${end.year}"
    } else {
        "${start.dayOfMonth} $startMonth ${start.year} – ${end.dayOfMonth} $endMonth ${end.year}"
    }
}

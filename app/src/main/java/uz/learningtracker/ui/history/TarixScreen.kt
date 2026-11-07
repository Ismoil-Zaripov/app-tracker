package uz.learningtracker.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uz.learningtracker.R
import uz.learningtracker.domain.model.DailyEntry
import uz.learningtracker.ui.components.EmptyState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("d-MMM, yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE")
private val filterDateFormatter = DateTimeFormatter.ofPattern("d-MMM")

@Composable
fun TarixScreen(
    viewModel: HistoryViewModel,
    onEditEntry: (LocalDate) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    TarixContent(
        state = uiState,
        onSetFilter = { from, to -> viewModel.setFilter(from, to) },
        onClearFilter = { viewModel.clearFilter() },
        onEditEntry = onEditEntry,
        onRequestDelete = { viewModel.requestDelete(it) },
        onCancelDelete = { viewModel.cancelDelete() },
        onConfirmDelete = { viewModel.confirmDelete() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TarixContent(
    state: HistoryUiState,
    onSetFilter: (LocalDate, LocalDate) -> Unit,
    onClearFilter: () -> Unit,
    onEditEntry: (LocalDate) -> Unit,
    onRequestDelete: (DailyEntry) -> Unit,
    onCancelDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
) {
    var showDateRangePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        FilterRow(
            filterFrom = state.filterFrom,
            filterTo = state.filterTo,
            entryCount = state.entries.size,
            onFilterClick = { showDateRangePicker = true },
            onClearFilter = onClearFilter,
        )

        if (state.entries.isEmpty() && !state.isLoading) {
            EmptyState(
                icon = Icons.AutoMirrored.Outlined.MenuBook,
                title = stringResource(R.string.tarix_empty_title),
                subtitle = stringResource(R.string.tarix_empty_subtitle),
                modifier = Modifier.weight(1f),
                action = {
                    if (state.filterFrom != null || state.filterTo != null) {
                        TextButton(onClick = onClearFilter) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(stringResource(R.string.tarix_clear_filter))
                        }
                    } else {
                        Button(onClick = { onEditEntry(LocalDate.now()) }) {
                            Text(stringResource(R.string.tarix_add_entry))
                        }
                    }
                },
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
            ) {
                items(state.entries, key = { it.id }) { entry ->
                    EntryListItem(
                        entry = entry,
                        isToday = entry.date == LocalDate.now(),
                        onClick = { onEditEntry(entry.date) },
                        onDelete = { onRequestDelete(entry) },
                    )
                }
            }
        }
    }

    if (showDateRangePicker) {
        DateRangePickerDialog(
            onConfirm = { from, to ->
                onSetFilter(from, to)
                showDateRangePicker = false
            },
            onDismiss = { showDateRangePicker = false },
        )
    }

    state.entryToDelete?.let { entry ->
        ConfirmDeleteDialog(
            entry = entry,
            onConfirm = onConfirmDelete,
            onDismiss = onCancelDelete,
        )
    }
}

@Composable
private fun FilterRow(
    filterFrom: LocalDate?,
    filterTo: LocalDate?,
    entryCount: Int,
    onFilterClick: () -> Unit,
    onClearFilter: () -> Unit,
) {
    val isFiltered = filterFrom != null || filterTo != null
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            FilterChip(
                selected = isFiltered,
                onClick = onFilterClick,
                label = {
                    if (isFiltered && filterFrom != null && filterTo != null) {
                        Text("${filterFrom.format(filterDateFormatter)} — ${filterTo.format(filterDateFormatter)}")
                    } else {
                        Text(stringResource(R.string.tarix_filter_label))
                    }
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                    )
                },
                trailingIcon = if (isFiltered) {
                    {
                        IconButton(
                            onClick = onClearFilter,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        ) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = stringResource(R.string.tarix_filter_clear_cd),
                                modifier = Modifier.size(FilterChipDefaults.IconSize),
                            )
                        }
                    }
                } else null,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.tarix_total_entries, entryCount),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EntryListItem(
    entry: DailyEntry,
    isToday: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val savedTime = Instant.ofEpochMilli(entry.updatedAt)
        .atZone(ZoneId.systemDefault()).toLocalDateTime()
    val dayName = savedTime.format(dayOfWeekFormatter)
    val timeStr = savedTime.format(timeFormatter)

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = entry.date.format(dateFormatter),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        if (isToday) {
                            Spacer(Modifier.width(8.dp))
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        stringResource(R.string.tarix_today_badge),
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                ),
                                border = null,
                            )
                        }
                    }
                    Text(
                        text = stringResource(R.string.tarix_saved_at, dayName, timeStr),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.tarix_delete_icon_cd),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = entry.mainTopic,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            stringResource(R.string.tarix_hours_format, formatHours(entry.hoursWorked)),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(AssistChipDefaults.IconSize),
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                    border = null,
                )
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            stringResource(R.string.tarix_tasks_format, entry.tasksCompleted),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                    border = null,
                )
            }

            Spacer(Modifier.height(8.dp))
            RatingRow(entry = entry)

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(
                    onClick = onClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(stringResource(R.string.tarix_edit), style = MaterialTheme.typography.labelLarge)
                }
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(stringResource(R.string.tarix_delete), style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun RatingRow(entry: DailyEntry) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        RatingBadge(label = stringResource(R.string.entry_energy_label), value = entry.energy)
        RatingBadge(label = stringResource(R.string.entry_focus_label), value = entry.focus)
        RatingBadge(label = stringResource(R.string.entry_understanding_label), value = entry.understanding)
    }
}

@Composable
private fun RatingBadge(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.tarix_rating_format, value),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ConfirmDeleteDialog(
    entry: DailyEntry,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val dateStr = entry.date.format(dateFormatter)
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Outlined.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
            )
        },
        title = {
            Text(
                text = stringResource(R.string.tarix_delete_dialog_title),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.tarix_delete_dialog_text, dateStr, entry.mainTopic),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.tarix_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.tarix_cancel))
            }
        },
        shape = MaterialTheme.shapes.extraLarge,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePickerDialog(
    onConfirm: (LocalDate, LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val pickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val startMs = pickerState.selectedStartDateMillis
                    val endMs = pickerState.selectedEndDateMillis
                    if (startMs != null && endMs != null) {
                        val from = Instant.ofEpochMilli(startMs).atZone(ZoneId.of("UTC")).toLocalDate()
                        val to = Instant.ofEpochMilli(endMs).atZone(ZoneId.of("UTC")).toLocalDate()
                        onConfirm(from, to)
                    }
                },
                enabled = pickerState.selectedStartDateMillis != null && pickerState.selectedEndDateMillis != null,
            ) {
                Text(stringResource(R.string.tarix_filter_label))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.tarix_cancel)) }
        },
    ) {
        DateRangePicker(state = pickerState, modifier = Modifier.weight(1f))
    }
}

private fun formatHours(hours: Double): String =
    if (hours == hours.toLong().toDouble()) hours.toLong().toString()
    else hours.toString()

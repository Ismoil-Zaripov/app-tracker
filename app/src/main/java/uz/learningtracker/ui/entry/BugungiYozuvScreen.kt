package uz.learningtracker.ui.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.learningtracker.R
import uz.learningtracker.ui.components.DatePickerField
import uz.learningtracker.ui.components.LabeledTextField
import uz.learningtracker.ui.components.RatingSelector
import uz.learningtracker.ui.theme.Spacing
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun BugungiYozuvScreen(
    viewModel: EntryViewModel,
    modifier: Modifier = Modifier,
    onSaveSuccess: () -> Unit,
    onCancelEdit: (() -> Unit)? = null,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.savedEvent) {
        if (state.savedEvent) {
            onSaveSuccess()
            viewModel.onSaveEventConsumed()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Spacer(Modifier.height(Spacing.sm))

            if (state.isEditMode) {
                EditModeHeader(lastUpdatedAt = state.lastUpdatedAt)
            }

            DatePickerField(
                date = state.date,
                onDateChange = viewModel::onDateChange,
                label = stringResource(R.string.entry_date_label),
                isToday = state.date == LocalDate.now(),
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                modifier = Modifier.fillMaxWidth(),
            ) {
                LabeledTextField(
                    value = state.hoursText,
                    onValueChange = viewModel::onHoursChange,
                    label = stringResource(R.string.entry_hours_label),
                    placeholder = stringResource(R.string.entry_hours_placeholder),
                    errorMessage = state.hoursError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.weight(1f),
                )
                LabeledTextField(
                    value = state.tasksText,
                    onValueChange = viewModel::onTasksChange,
                    label = stringResource(R.string.entry_tasks_label),
                    placeholder = stringResource(R.string.entry_tasks_placeholder),
                    errorMessage = state.tasksError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.weight(1f),
                )
            }

            LabeledTextField(
                value = state.mainTopic,
                onValueChange = viewModel::onTopicChange,
                label = stringResource(R.string.entry_topic_label),
                placeholder = stringResource(R.string.entry_topic_placeholder),
                errorMessage = state.topicError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
            )

            RatingsCard(state = state, viewModel = viewModel)

            ReflectionHeader()

            LabeledTextField(
                value = state.biggestWin,
                onValueChange = viewModel::onWinChange,
                label = stringResource(R.string.entry_win_label),
                placeholder = stringResource(R.string.entry_win_placeholder),
                singleLine = false,
                maxLines = 5,
                minLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
            )

            LabeledTextField(
                value = state.biggestDifficulty,
                onValueChange = viewModel::onDifficultyChange,
                label = stringResource(R.string.entry_difficulty_label),
                placeholder = stringResource(R.string.entry_difficulty_placeholder),
                singleLine = false,
                maxLines = 5,
                minLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(88.dp))
        }

        StickyBottomActions(
            isEditMode = state.isEditMode,
            onSave = viewModel::onSave,
            onCancel = onCancelEdit,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun EditModeHeader(lastUpdatedAt: Long?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.entry_edit_mode_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (lastUpdatedAt != null) {
                val time = java.time.Instant.ofEpochMilli(lastUpdatedAt)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                Text(
                    text = stringResource(R.string.entry_last_updated, time),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        SuggestionChip(
            onClick = {},
            label = { Text(stringResource(R.string.entry_edit_mode_badge)) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                )
            },
            colors = SuggestionChipDefaults.suggestionChipColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                iconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
        )
    }
}

@Composable
private fun ReflectionHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = Modifier.fillMaxWidth(),
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.AutoStories,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.entry_reflection_title),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun RatingsCard(state: EntryUiState, viewModel: EntryViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(Spacing.md),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.entry_ratings_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.entry_ratings_range),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = stringResource(R.string.entry_ratings_subtitle),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        RatingRow(
            label = stringResource(R.string.entry_understanding_label),
            value = state.understanding,
            onValueChange = viewModel::onUnderstandingChange,
        )
        RatingRow(
            label = stringResource(R.string.entry_energy_label),
            value = state.energy,
            onValueChange = viewModel::onEnergyChange,
        )
        RatingRow(
            label = stringResource(R.string.entry_focus_label),
            value = state.focus,
            onValueChange = viewModel::onFocusChange,
        )
    }
}

@Composable
private fun RatingRow(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "$value/5",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        RatingSelector(
            value = value,
            onValueChange = onValueChange,
            contentDesc = label,
        )
    }
}

@Composable
private fun StickyBottomActions(
    isEditMode: Boolean,
    onSave: () -> Unit,
    onCancel: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onSave,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.padding(end = Spacing.xs),
            )
            Text(
                text = if (isEditMode) stringResource(R.string.entry_save_changes_button)
                else stringResource(R.string.entry_save_button),
                style = MaterialTheme.typography.labelLarge,
            )
        }
        if (isEditMode && onCancel != null) {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.entry_cancel_changes))
            }
        }
    }
}

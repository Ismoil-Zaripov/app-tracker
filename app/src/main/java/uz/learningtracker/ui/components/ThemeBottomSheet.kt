package uz.learningtracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uz.learningtracker.R
import uz.learningtracker.domain.ThemeMode
import uz.learningtracker.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeBottomSheet(
    currentMode: ThemeMode,
    onThemeSelect: (ThemeMode) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Text(
            text = stringResource(R.string.theme_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
        )

        ThemeOption(
            label = stringResource(R.string.theme_system),
            selected = currentMode == ThemeMode.SYSTEM,
            onClick = { onThemeSelect(ThemeMode.SYSTEM) },
        )
        ThemeOption(
            label = stringResource(R.string.theme_light),
            selected = currentMode == ThemeMode.LIGHT,
            onClick = { onThemeSelect(ThemeMode.LIGHT) },
        )
        ThemeOption(
            label = stringResource(R.string.theme_dark),
            selected = currentMode == ThemeMode.DARK,
            onClick = { onThemeSelect(ThemeMode.DARK) },
        )

        Spacer(modifier = Modifier.padding(bottom = Spacing.lg))
    }
}

@Composable
private fun ThemeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.md, vertical = Spacing.md),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        if (selected) {
            Spacer(modifier = Modifier.width(Spacing.sm))
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

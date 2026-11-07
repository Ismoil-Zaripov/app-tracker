package uz.learningtracker.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun RatingSelector(
    value: Int,
    onValueChange: (Int) -> Unit,
    contentDesc: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        for (i in 1..5) {
            RatingCircle(
                number = i,
                selected = i == value,
                onClick = { onValueChange(i) },
                contentDesc = "$contentDesc $i",
            )
        }
    }
}

@Composable
private fun RatingCircle(
    number: Int,
    selected: Boolean,
    onClick: () -> Unit,
    contentDesc: String,
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val surfaceContainer = MaterialTheme.colorScheme.surfaceContainer
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    val bgColor = if (selected) primary else surfaceContainer
    val textColor = if (selected) onPrimary else onSurfaceVariant

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .drawBehind { drawCircle(bgColor) }
            .then(
                if (!selected) Modifier.border(1.dp, outlineVariant, CircleShape) else Modifier
            )
            .clickable { onClick() }
            .semantics { contentDescription = contentDesc },
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
        )
    }
}

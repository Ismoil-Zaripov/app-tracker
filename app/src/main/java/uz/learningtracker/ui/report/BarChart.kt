package uz.learningtracker.ui.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(
    values: List<Double>,
    labels: List<String>,
    barColor: Color,
    trackColor: Color,
    labelStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()

    Canvas(modifier = modifier.fillMaxWidth().height(140.dp)) {
        val count = values.size.coerceAtLeast(1)
        val maxVal = values.maxOrNull()?.coerceAtLeast(0.1) ?: 0.1
        val labelHeight = 20.dp.toPx()
        val chartHeight = size.height - labelHeight
        val totalGap = size.width * 0.3f
        val barWidth = (size.width - totalGap) / count
        val gapWidth = totalGap / (count + 1)

        values.forEachIndexed { i, value ->
            val x = gapWidth * (i + 1) + barWidth * i
            val barH = (value / maxVal * chartHeight * 0.85f).toFloat().coerceAtLeast(4.dp.toPx())
            val top = chartHeight - barH

            // Track (background bar)
            drawRoundRect(
                color = trackColor,
                topLeft = Offset(x, 0f),
                size = Size(barWidth, chartHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2),
            )
            // Value bar
            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, top),
                size = Size(barWidth, barH),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2),
            )
            // Label
            if (i < labels.size) {
                val result = measurer.measure(labels[i], labelStyle)
                val labelX = x + barWidth / 2 - result.size.width / 2
                drawText(
                    textLayoutResult = result,
                    topLeft = Offset(labelX, chartHeight + 4.dp.toPx()),
                )
            }
        }
    }
}

package uz.learningtracker.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(
    energyPoints: List<Float?>,
    focusPoints: List<Float?>,
    xLabels: List<String>,
    modifier: Modifier = Modifier,
) {
    val focusColor = MaterialTheme.colorScheme.primary
    val energyColor = MaterialTheme.colorScheme.tertiary
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val labelStyle = MaterialTheme.typography.labelSmall
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
    ) {
        val bottomPad = 20.dp.toPx()
        val topPad = 8.dp.toPx()
        val chartBottom = size.height - bottomPad
        val chartTop = topPad
        val chartHeight = chartBottom - chartTop
        val n = xLabels.size.coerceAtLeast(2)

        fun xOf(i: Int): Float = if (n > 1) i.toFloat() / (n - 1) * size.width else size.width / 2f
        fun yOf(v: Float): Float = chartBottom - ((v.coerceIn(1f, 5f) - 1f) / 4f) * chartHeight

        // Grid lines
        for (level in 1..5) {
            drawLine(
                color = gridColor.copy(alpha = 0.5f),
                start = Offset(0f, yOf(level.toFloat())),
                end = Offset(size.width, yOf(level.toFloat())),
                strokeWidth = 0.5.dp.toPx(),
            )
        }

        drawSeries(energyPoints, energyColor, n, ::xOf, ::yOf, chartBottom)
        drawSeries(focusPoints, focusColor, n, ::xOf, ::yOf, chartBottom)

        // X-axis labels
        val labelStep = if (n <= 7) 1 else 5
        for (i in xLabels.indices) {
            if (n > 7 && i % labelStep != 0 && i != xLabels.lastIndex) continue
            val label = xLabels[i]
            val measured = textMeasurer.measure(
                AnnotatedString(label),
                style = TextStyle(
                    fontSize = labelStyle.fontSize,
                    fontWeight = labelStyle.fontWeight,
                    color = labelColor,
                ),
            )
            val x = (xOf(i) - measured.size.width / 2f).coerceIn(0f, size.width - measured.size.width)
            val y = size.height - measured.size.height
            drawText(measured, topLeft = Offset(x, y))
        }
    }
}

private fun DrawScope.drawSeries(
    points: List<Float?>,
    color: Color,
    n: Int,
    xOf: (Int) -> Float,
    yOf: (Float) -> Float,
    chartBottom: Float,
) {
    val firstIdx = points.indexOfFirst { it != null }
    val lastIdx = points.indexOfLast { it != null }
    if (firstIdx < 0) return

    val fillPath = Path().apply {
        moveTo(xOf(firstIdx), chartBottom)
        for (i in firstIdx..lastIdx) {
            val v = points[i] ?: continue
            lineTo(xOf(i), yOf(v))
        }
        lineTo(xOf(lastIdx), chartBottom)
        close()
    }
    drawPath(fillPath, color.copy(alpha = 0.12f))

    val linePath = Path()
    var lineStarted = false
    for (i in firstIdx..lastIdx) {
        val v = points[i] ?: continue
        if (!lineStarted) {
            linePath.moveTo(xOf(i), yOf(v))
            lineStarted = true
        } else {
            linePath.lineTo(xOf(i), yOf(v))
        }
    }
    drawPath(
        linePath,
        color,
        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
    )

    for (i in firstIdx..lastIdx) {
        val v = points[i] ?: continue
        val center = Offset(xOf(i), yOf(v))
        drawCircle(color, radius = 4.dp.toPx(), center = center)
        drawCircle(androidx.compose.ui.graphics.Color.White, radius = 2.dp.toPx(), center = center)
    }
}

package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TaskPriority
import com.example.model.TaskStatus
import com.example.ui.theme.*

@Composable
fun MetricStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
    trendPositive: Boolean? = null,
    trendText: String? = null
) {
    Card(
        modifier = modifier
            .testTag("metric_card_${title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (trendText != null) {
                    val badgeBg = if (trendPositive == true) StatusSuccess.copy(alpha = 0.15f) else StatusDanger.copy(alpha = 0.15f)
                    val badgeColor = if (trendPositive == true) StatusSuccess else StatusDanger
                    Surface(
                        color = badgeBg,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = trendText,
                            color = badgeColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TaskPriorityBadge(priority: TaskPriority) {
    val (color, text) = when (priority) {
        TaskPriority.LOW -> StatusInfo to "LOW"
        TaskPriority.MEDIUM -> BrandSecondary to "MED"
        TaskPriority.HIGH -> StatusWarning to "HIGH"
        TaskPriority.CRITICAL -> StatusDanger to "CRITICAL"
    }
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(6.dp),
        border = null
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun TaskStatusBadge(status: TaskStatus) {
    val (bg, fg, label) = when (status) {
        TaskStatus.TODO -> Triple(Color(0xFF64748B).copy(alpha = 0.12f), Color(0xFF64748B), "To Do")
        TaskStatus.IN_PROGRESS -> Triple(BrandPrimary.copy(alpha = 0.12f), BrandPrimary, "In Progress")
        TaskStatus.COMPLETED -> Triple(StatusSuccess.copy(alpha = 0.12f), StatusSuccess, "Completed")
        TaskStatus.OVERDUE -> Triple(StatusDanger.copy(alpha = 0.12f), StatusDanger, "Overdue")
    }
    Surface(
        color = bg,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = label,
            color = fg,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
        }
        if (actionButtonText != null && onActionClick != null) {
            FilledTonalButton(
                onClick = onActionClick,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(actionButtonText, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun InteractiveTrendLineChart(
    points: List<Float>,
    labels: List<String>,
    lineColor: Color = BrandPrimary,
    modifier: Modifier = Modifier.fillMaxWidth().height(160.dp)
) {
    Canvas(modifier = modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
        if (points.isEmpty()) return@Canvas
        val maxVal = (points.maxOrNull() ?: 100f).coerceAtLeast(10f)
        val minVal = (points.minOrNull() ?: 0f).coerceAtMost(0f)
        val range = (maxVal - minVal).coerceAtLeast(1f)

        val stepX = size.width / (points.size - 1).coerceAtLeast(1)
        val path = Path()

        points.forEachIndexed { i, p ->
            val x = i * stepX
            val normalizedY = (p - minVal) / range
            val y = size.height - (normalizedY * (size.height * 0.8f)) - (size.height * 0.1f)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        // Draw horizontal grid lines
        for (g in 0..3) {
            val gy = size.height * (g / 3f)
            drawLine(
                color = Color.Gray.copy(alpha = 0.15f),
                start = Offset(0f, gy),
                end = Offset(size.width, gy),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Draw line
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw points
        points.forEachIndexed { i, p ->
            val x = i * stepX
            val normalizedY = (p - minVal) / range
            val y = size.height - (normalizedY * (size.height * 0.8f)) - (size.height * 0.1f)
            drawCircle(
                color = Color.White,
                radius = 5.dp.toPx(),
                center = Offset(x, y)
            )
            drawCircle(
                color = lineColor,
                radius = 3.5.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun MiniBarChart(
    data: List<Pair<String, Float>>,
    barColor: Color = BrandSecondary,
    modifier: Modifier = Modifier.fillMaxWidth().height(140.dp)
) {
    Canvas(modifier = modifier.padding(vertical = 8.dp)) {
        if (data.isEmpty()) return@Canvas
        val maxVal = data.maxOfOrNull { it.second } ?: 10f
        val barWidth = (size.width / data.size) * 0.55f
        val stepX = size.width / data.size

        data.forEachIndexed { i, pair ->
            val barHeight = (pair.second / maxVal) * (size.height * 0.85f)
            val left = i * stepX + (stepX - barWidth) / 2f
            val top = size.height - barHeight

            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
        }
    }
}

@Composable
fun DonutProgressChart(
    percentage: Int,
    strokeWidth: Dp = 12.dp,
    activeColor: Color = BrandPrimary,
    trackColor: Color = BrandPrimary.copy(alpha = 0.12f),
    modifier: Modifier = Modifier.size(110.dp)
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val arcSize = size.width - strokePx
            val topLeftOffset = Offset(strokePx / 2f, strokePx / 2f)

            // Background circle
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeftOffset,
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Value arc
            val sweep = (percentage / 100f) * 360f
            drawArc(
                color = activeColor,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeftOffset,
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Score",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

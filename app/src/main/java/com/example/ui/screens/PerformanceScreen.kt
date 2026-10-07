package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EmployeePerformance
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun PerformanceScreen(
    performanceList: List<EmployeePerformance>,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All Teams") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Performance Metrics
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Organizational Productivity & Wellbeing Index",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Focus on sustainable velocity, quality outcomes, and healthy workload boundaries",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MetricStatCard(
                            title = "Avg Team Score",
                            value = "91.8%",
                            subtitle = "balanced workload",
                            icon = Icons.Default.TrendingUp,
                            iconTint = BrandPrimary,
                            trendPositive = true,
                            trendText = "+5%",
                            modifier = Modifier.weight(1f)
                        )
                        MetricStatCard(
                            title = "On-Time Rate",
                            value = "94.2%",
                            subtitle = "sprint commitments",
                            icon = Icons.Default.Timer,
                            iconTint = StatusSuccess,
                            modifier = Modifier.weight(1f)
                        )
                        MetricStatCard(
                            title = "Attendance Health",
                            value = "98.5%",
                            subtitle = "sustainable hours",
                            icon = Icons.Default.FavoriteBorder,
                            iconTint = BrandSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Weekly Velocity Chart
                    Text(
                        text = "Quarterly Velocity Index",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    InteractiveTrendLineChart(
                        points = listOf(82f, 85f, 89f, 91f, 94f, 92f),
                        labels = listOf("W1", "W2", "W3", "W4", "W5", "W6"),
                        lineColor = BrandPrimary
                    )
                }
            }
        }

        // Wellbeing & Anti-Overwork Policy Note
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandPrimary.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(14.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.Spa, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(24.dp))
                    Text(
                        text = "Wellbeing Policy: WorkFlow AI optimizes for efficient deep work, preventing chronic overtime while maintaining high delivery quality.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        lineHeight = 17.sp
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "Employee Performance Breakdown",
                subtitle = "Individual throughput, cycle times, and support requirements"
            )
        }

        items(performanceList) { emp ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(BrandPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emp.name.split(" ").mapNotNull { it.firstOrNull() }.joinToString(""),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = emp.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                if (emp.needsSupport) {
                                    Surface(
                                        color = StatusWarning.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            "Support Alert",
                                            color = StatusWarning,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "${emp.role} • ${emp.department}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Avg Task: ${emp.avgTaskHours}h | On-time: ${emp.onTimeCompletionRate}% | Completed: ${emp.tasksCompleted}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${emp.productivityScore}%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (emp.productivityScore >= 90) StatusSuccess else BrandPrimary
                        )
                        Text(
                            text = "Score",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

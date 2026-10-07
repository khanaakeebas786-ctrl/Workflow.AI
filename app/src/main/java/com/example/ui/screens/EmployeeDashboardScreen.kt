package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TaskItem
import com.example.model.TaskStatus
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.AppUiState

@Composable
fun EmployeeDashboardScreen(
    state: AppUiState,
    onNavigate: (AppScreen) -> Unit,
    onUpdateTaskStatus: (String, TaskStatus) -> Unit,
    onToggleCheckIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = state.tasks.count { it.status == TaskStatus.COMPLETED }
    val pendingCount = state.tasks.count { it.status == TaskStatus.TODO || it.status == TaskStatus.IN_PROGRESS }
    val overdueCount = state.tasks.count { it.status == TaskStatus.OVERDUE }
    val totalAssigned = state.tasks.size

    val productivityScore = 94 // Percent

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Banner Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Good Morning, ${state.currentUser?.name ?: "Employee"}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${state.currentUser?.title} • ${state.currentUser?.department}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = if (state.isCheckedIn) StatusSuccess.copy(alpha = 0.2f) else StatusWarning.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (state.isCheckedIn) "● Active Work Session" else "○ Checked Out",
                                    color = if (state.isCheckedIn) StatusSuccess else StatusWarning,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Today: ${state.todayHoursWorked} hrs logged",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Button(
                        onClick = onToggleCheckIn,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isCheckedIn) StatusDanger else StatusSuccess
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (state.isCheckedIn) "Check Out" else "Check In", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 6 Overview Metric Cards in 2 rows of 3 columns
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricStatCard(
                        title = "Tasks Assigned",
                        value = "$totalAssigned",
                        subtitle = "in active sprint",
                        icon = Icons.Default.Assignment,
                        iconTint = BrandPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Tasks Completed",
                        value = "$completedCount",
                        subtitle = "${(completedCount * 100) / totalAssigned.coerceAtLeast(1)}% complete",
                        icon = Icons.Default.CheckCircle,
                        iconTint = StatusSuccess,
                        trendPositive = true,
                        trendText = "+24%",
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Pending Tasks",
                        value = "$pendingCount",
                        subtitle = "$overdueCount overdue",
                        icon = Icons.Default.PendingActions,
                        iconTint = StatusWarning,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricStatCard(
                        title = "Productivity Score",
                        value = "$productivityScore%",
                        subtitle = "+6% above median",
                        icon = Icons.Default.TrendingUp,
                        iconTint = BrandAccent,
                        trendPositive = true,
                        trendText = "High",
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Attendance Rate",
                        value = "99.2%",
                        subtitle = "7/7 days on time",
                        icon = Icons.Default.AccessTime,
                        iconTint = BrandSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Avg Completion",
                        value = "4.2 hrs",
                        subtitle = "per story ticket",
                        icon = Icons.Default.Speed,
                        iconTint = Color(0xFFF97316),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Productivity Analytics Section with Charts
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "Productivity Trends & Velocity",
                        subtitle = "Daily, Weekly, and Monthly sprint throughput telemetry",
                        actionButtonText = "Full Reports",
                        onActionClick = { onNavigate(AppScreen.REPORTS_ANALYTICS) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Donut Score
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(130.dp)
                        ) {
                            DonutProgressChart(percentage = productivityScore)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Daily Health",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Weekly Line Chart
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Weekly Output Velocity",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    "Mon - Fri",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            InteractiveTrendLineChart(
                                points = listOf(65f, 78f, 85f, 92f, 94f),
                                labels = listOf("M", "T", "W", "T", "F")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mini Stats Footer row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Weekly Trend", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("+18.4%", fontWeight = FontWeight.Bold, color = StatusSuccess, fontSize = 13.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Monthly Trend", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("+12.1%", fontWeight = FontWeight.Bold, color = BrandPrimary, fontSize = 13.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Workload Level", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Balanced", fontWeight = FontWeight.Bold, color = BrandSecondary, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // AI Assistant Quick-Trigger Callout
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandPrimary.copy(alpha = 0.08f)),
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BrandPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                        }
                        Column {
                            Text(
                                "AI Daily Schedule Suggestion Ready",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                "Prioritized 2 critical security items before sprint freeze at 4 PM.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                    Button(
                        onClick = { onNavigate(AppScreen.AI_ASSISTANT) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Text("View AI Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active Tasks section
        item {
            SectionHeader(
                title = "Today's Assigned Tasks",
                subtitle = "Manage active tickets and move statuses directly",
                actionButtonText = "Kanban Board",
                onActionClick = { onNavigate(AppScreen.TASK_MANAGEMENT) }
            )
        }

        items(state.tasks.take(4)) { task ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("task_row_${task.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TaskPriorityBadge(task.priority)
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = task.category,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        TaskStatusBadge(task.status)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                                Text(task.deadline, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                                Text(task.projectName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                            }
                        }

                        // Quick Status advance button
                        if (task.status != TaskStatus.COMPLETED) {
                            FilledTonalButton(
                                onClick = {
                                    val nextStatus = if (task.status == TaskStatus.TODO) TaskStatus.IN_PROGRESS else TaskStatus.COMPLETED
                                    onUpdateTaskStatus(task.id, nextStatus)
                                },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    if (task.status == TaskStatus.TODO) "Start" else "Mark Done",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

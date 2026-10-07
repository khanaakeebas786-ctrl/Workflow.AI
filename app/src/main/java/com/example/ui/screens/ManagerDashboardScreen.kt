package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.TaskItem
import com.example.model.TaskPriority
import com.example.model.TaskStatus
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.AppUiState

@Composable
fun ManagerDashboardScreen(
    state: AppUiState,
    onNavigate: (AppScreen) -> Unit,
    onReassignTask: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalEmployees = state.teamPerformance.size
    val activeEmployees = totalEmployees
    val tasksCompleted = state.tasks.count { it.status == TaskStatus.COMPLETED }
    val tasksPending = state.tasks.count { it.status != TaskStatus.COMPLETED }
    val tasksOverdue = state.tasks.count { it.status == TaskStatus.OVERDUE }

    var reassignDialogTask by remember { mutableStateOf<TaskItem?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Manager Executive Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder(),
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
                            text = "Engineering & Operations Leadership",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Team Velocity: +18.4% this sprint  •  Capacity Utilization: 88% Optimal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Button(
                        onClick = { onNavigate(AppScreen.TASK_MANAGEMENT) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Assign Task", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 6 Core Manager Stats Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricStatCard(
                        title = "Total Employees",
                        value = "$totalEmployees",
                        subtitle = "$activeEmployees currently active",
                        icon = Icons.Default.Groups,
                        iconTint = BrandPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Tasks Completed",
                        value = "$tasksCompleted",
                        subtitle = "+18% this sprint",
                        icon = Icons.Default.TaskAlt,
                        iconTint = StatusSuccess,
                        trendPositive = true,
                        trendText = "High",
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Pending Tasks",
                        value = "$tasksPending",
                        subtitle = "$tasksOverdue overdue tickets",
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
                        title = "Team Productivity",
                        value = "93.4%",
                        subtitle = "across all 4 squads",
                        icon = Icons.Default.Speed,
                        iconTint = BrandAccent,
                        trendPositive = true,
                        trendText = "+4.2%",
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Support Needed",
                        value = "2 Members",
                        subtitle = "high workload alert",
                        icon = Icons.Default.SupportAgent,
                        iconTint = StatusDanger,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Active Workflows",
                        value = "${state.workflows.size}",
                        subtitle = "1 delayed pipeline",
                        icon = Icons.Default.AccountTree,
                        iconTint = BrandSecondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Team Workload Distribution Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "Team Workload & Distribution",
                        subtitle = "Real-time task volume per engineering specialist",
                        actionButtonText = "Performance",
                        onActionClick = { onNavigate(AppScreen.PERFORMANCE) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    MiniBarChart(
                        data = state.teamPerformance.map { it.name.split(" ")[0] to it.tasksCompleted.toFloat() },
                        barColor = BrandPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bottleneck banner
                    Surface(
                        color = StatusWarning.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = StatusWarning)
                            Text(
                                text = "Workload Imbalance Alert: Marcus Vance (DevOps) and Jordan Lee (QA) have 30% higher active ticket load than average. Consider redistributing pending tickets.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Active Tasks delegation table
        item {
            SectionHeader(
                title = "Team Tasks & Delegation Control",
                subtitle = "Reassign bottlenecks and balance employee workloads"
            )
        }

        items(state.tasks) { task ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TaskPriorityBadge(task.priority)
                            TaskStatusBadge(task.status)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Assignee: ${task.assignedToName}  |  Due: ${task.deadline}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }

                    OutlinedButton(
                        onClick = { reassignDialogTask = task },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reassign", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (reassignDialogTask != null) {
        val task = reassignDialogTask!!
        var selectedName by remember { mutableStateOf(state.teamPerformance[0].name) }

        AlertDialog(
            onDismissRequest = { reassignDialogTask = null },
            title = { Text("Reassign Task") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select team member to assign '${task.title}':", fontSize = 13.sp)
                    state.teamPerformance.forEach { emp ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedName == emp.name,
                                onClick = { selectedName = emp.name }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${emp.name} (${emp.role})", fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    onReassignTask(task.id, selectedName)
                    reassignDialogTask = null
                }) {
                    Text("Confirm Reassignment")
                }
            },
            dismissButton = {
                TextButton(onClick = { reassignDialogTask = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

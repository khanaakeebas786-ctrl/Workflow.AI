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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WorkflowItem
import com.example.model.WorkflowStatus
import com.example.ui.components.MetricStatCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun WorkflowManagementScreen(
    workflows: List<WorkflowItem>,
    onAdvanceStage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeCount = workflows.count { it.status == WorkflowStatus.ACTIVE }
    val completedCount = workflows.count { it.status == WorkflowStatus.COMPLETED }
    val delayedCount = workflows.count { it.status == WorkflowStatus.DELAYED }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overview Metric Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricStatCard(
                    title = "Active Workflows",
                    value = "$activeCount",
                    subtitle = "in production pipeline",
                    icon = Icons.Default.AccountTree,
                    iconTint = BrandPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    title = "Completed Workflows",
                    value = "$completedCount",
                    subtitle = "successfully validated",
                    icon = Icons.Default.CheckCircle,
                    iconTint = StatusSuccess,
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    title = "Delayed Workflows",
                    value = "$delayedCount",
                    subtitle = "requiring unblocking",
                    icon = Icons.Default.Warning,
                    iconTint = StatusDanger,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Bottlenecks & Efficiency Header
        item {
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Pipeline Health: 91.4% Efficiency",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Primary Bottleneck: QA Automated Regression & External Legal Review",
                            style = MaterialTheme.typography.bodySmall,
                            color = StatusDanger
                        )
                    }

                    Surface(
                        color = BrandPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Avg 38.2 hrs / Cycle",
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        item {
            SectionHeader(
                title = "Enterprise Workflow Pipelines",
                subtitle = "Multi-stage lifecycle orchestration with approval gates"
            )
        }

        items(workflows) { wf ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Title and Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = wf.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${wf.department} • Avg ${wf.avgCompletionTimeHours} hrs",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }

                        Surface(
                            color = when (wf.status) {
                                WorkflowStatus.ACTIVE -> BrandPrimary.copy(alpha = 0.12f)
                                WorkflowStatus.COMPLETED -> StatusSuccess.copy(alpha = 0.12f)
                                WorkflowStatus.DELAYED -> StatusDanger.copy(alpha = 0.12f)
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = wf.status.displayName,
                                color = when (wf.status) {
                                    WorkflowStatus.ACTIVE -> BrandPrimary
                                    WorkflowStatus.COMPLETED -> StatusSuccess
                                    WorkflowStatus.DELAYED -> StatusDanger
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stages visualization
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        wf.stages.forEachIndexed { index, stage ->
                            val isCompleted = stage.isCompleted
                            val isCurrent = index == wf.activeStageIndex && wf.status != WorkflowStatus.COMPLETED

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isCurrent) BrandPrimary.copy(alpha = 0.06f) else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 4.dp, horizontal = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isCompleted -> StatusSuccess
                                                isCurrent -> BrandPrimary
                                                else -> Color.Gray.copy(alpha = 0.3f)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isCompleted) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    } else {
                                        Text("${index + 1}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stage.name,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Role: ${stage.assignedRole}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }

                                if (isCurrent) {
                                    Surface(
                                        color = BrandPrimary,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            "In Review",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bottleneck banner if any
                    if (wf.bottleneckStage != null) {
                        Surface(
                            color = StatusDanger.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.HourglassTop, contentDescription = null, tint = StatusDanger, modifier = Modifier.size(16.dp))
                                Text(
                                    "Bottleneck Stage: ${wf.bottleneckStage}",
                                    color = StatusDanger,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Action buttons
                    if (wf.status != WorkflowStatus.COMPLETED) {
                        Button(
                            onClick = { onAdvanceStage(wf.id) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Approve & Advance Next Stage", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

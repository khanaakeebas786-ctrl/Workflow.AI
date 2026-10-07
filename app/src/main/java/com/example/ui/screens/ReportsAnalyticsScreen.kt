package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.InteractiveTrendLineChart
import com.example.ui.components.MiniBarChart
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.AppUiState

enum class ReportInterval { DAILY, WEEKLY, MONTHLY }

@Composable
fun ReportsAnalyticsScreen(
    state: AppUiState,
    onExportCsv: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedInterval by remember { mutableStateOf(ReportInterval.WEEKLY) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Filter bar & Export Button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Interval Switcher
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ReportInterval.values().forEach { interval ->
                        val isSelected = selectedInterval == interval
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) BrandPrimary else Color.Transparent,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        ) {
                            TextButton(
                                onClick = { selectedInterval = interval },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = interval.name.lowercase().replaceFirstChar { it.uppercase() },
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onExportCsv,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandSecondary)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export CSV/PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Employee Productivity Report Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "Employee Productivity & Sprint Velocity",
                        subtitle = "Cumulative task completions and story points delivered"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    InteractiveTrendLineChart(
                        points = when (selectedInterval) {
                            ReportInterval.DAILY -> listOf(78f, 84f, 82f, 90f, 94f)
                            ReportInterval.WEEKLY -> listOf(80f, 86f, 89f, 92f, 95f)
                            ReportInterval.MONTHLY -> listOf(72f, 79f, 85f, 91f, 96f)
                        },
                        labels = listOf("T1", "T2", "T3", "T4", "T5"),
                        lineColor = BrandPrimary
                    )
                }
            }
        }

        // Team Workload Efficiency Report Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "Team Output Distribution",
                        subtitle = "Completed task volumes by specialist"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    MiniBarChart(
                        data = state.teamPerformance.map { it.name.split(" ")[0] to it.tasksCompleted.toFloat() },
                        barColor = BrandAccent
                    )
                }
            }
        }

        // Attendance & Workflow Efficiency Report Table
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Executive Summary Metrics Table",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    state.departments.forEach { dept ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(dept.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("${dept.avgProductivity}% Output", color = BrandPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${dept.employeeCount} Specialists", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    }
                }
            }
        }
    }
}

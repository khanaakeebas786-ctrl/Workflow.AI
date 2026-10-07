package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MetricStatCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.AppScreen
import com.example.viewmodel.AppUiState

@Composable
fun AdminDashboardScreen(
    state: AppUiState,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Administrator Welcome Banner
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
                            text = "WorkFlow AI Administration Console",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Role-Based Access Control, Tenant Governance & Security Auditing",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilledTonalButton(
                            onClick = { onNavigate(AppScreen.USER_MANAGEMENT) },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Employee", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 6 Admin Statistics Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricStatCard(
                        title = "Total Employees",
                        value = "48",
                        subtitle = "across 5 business units",
                        icon = Icons.Default.Badge,
                        iconTint = BrandPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Total Managers",
                        value = "6",
                        subtitle = "active squad leaders",
                        icon = Icons.Default.SupervisorAccount,
                        iconTint = BrandSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Departments",
                        value = "${state.departments.size}",
                        subtitle = "configured units",
                        icon = Icons.Default.Business,
                        iconTint = BrandAccent,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricStatCard(
                        title = "Active Users",
                        value = "46 / 48",
                        subtitle = "95.8% active today",
                        icon = Icons.Default.CheckCircle,
                        iconTint = StatusSuccess,
                        trendPositive = true,
                        trendText = "Healthy",
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "System Security",
                        value = "SOC-2 OK",
                        subtitle = "Zero policy violations",
                        icon = Icons.Default.Shield,
                        iconTint = StatusSuccess,
                        modifier = Modifier.weight(1f)
                    )
                    MetricStatCard(
                        title = "Active Workflows",
                        value = "${state.workflows.size}",
                        subtitle = "14 running stages",
                        icon = Icons.Default.AltRoute,
                        iconTint = Color(0xFFF97316),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Departments Management Overview
        item {
            SectionHeader(
                title = "Department Productivity & Headcount",
                subtitle = "Manage organizational units and team leads",
                actionButtonText = "View Audit Logs",
                onActionClick = { onNavigate(AppScreen.SYSTEM_LOGS) }
            )
        }

        items(state.departments) { dept ->
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = dept.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Department Lead: ${dept.managerName}  •  ${dept.employeeCount} Members",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = StatusSuccess.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "${dept.avgProductivity}% Avg Productivity",
                                color = StatusSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(onClick = { /* edit department */ }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options")
                        }
                    }
                }
            }
        }

        // System Activity Logs Preview
        item {
            SectionHeader(
                title = "Recent System Security Logs",
                subtitle = "Access events, privilege changes, and token activities"
            )
        }

        items(state.systemLogs.take(3)) { log ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = BrandPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(log.action, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("${log.userEmail}  •  ${log.ipAddress}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            color = if (log.status == "SUCCESS") StatusSuccess.copy(alpha = 0.15f) else BrandPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                log.status,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (log.status == "SUCCESS") StatusSuccess else BrandPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(log.timestamp, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }
            }
        }
    }
}

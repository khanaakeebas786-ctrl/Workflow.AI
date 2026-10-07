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
import com.example.model.AttendanceRecord
import com.example.ui.components.MetricStatCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun AttendanceScreen(
    attendanceList: List<AttendanceRecord>,
    isCheckedIn: Boolean,
    todayHoursWorked: Double,
    onToggleCheckIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalWeeklyHours = attendanceList.take(5).sumOf { it.totalHours }
    val onTimePercentage = 98

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Attendance Punch Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Work Hours & Attendance Tracking",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Transparent, employee-first logging with zero invasive surveillance",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }

                        Button(
                            onClick = onToggleCheckIn,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCheckedIn) StatusDanger else StatusSuccess
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Icon(
                                imageVector = if (isCheckedIn) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                if (isCheckedIn) "Punch Out" else "Punch In",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 3 attendance metric pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricStatCard(
                            title = "Today Logged",
                            value = "${"%.1f".format(todayHoursWorked)} hrs",
                            subtitle = if (isCheckedIn) "active session" else "completed",
                            icon = Icons.Default.Timer,
                            iconTint = BrandPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        MetricStatCard(
                            title = "Weekly Total",
                            value = "${"%.1f".format(totalWeeklyHours)} hrs",
                            subtitle = "target: 40.0 hrs",
                            icon = Icons.Default.CalendarToday,
                            iconTint = BrandSecondary,
                            trendPositive = true,
                            trendText = "On Track",
                            modifier = Modifier.weight(1f)
                        )
                        MetricStatCard(
                            title = "Punctuality",
                            value = "$onTimePercentage%",
                            subtitle = "on-time arrival",
                            icon = Icons.Default.CheckCircle,
                            iconTint = StatusSuccess,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Ethical Surveillance Disclaimer Note
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandPrimary.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = BrandPrimary)
                    Text(
                        text = "Privacy Guarantee: WorkFlow AI tracks voluntary check-in/out and completed deliverables. Keystroke logging, webcam monitoring, and screen surveillance are strictly forbidden.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Attendance History Table
        item {
            SectionHeader(
                title = "Attendance History Log",
                subtitle = "Past work days, check-in timestamps, and logged durations"
            )
        }

        items(attendanceList) { record ->
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = record.dayOfWeek.take(3).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = BrandPrimary
                                )
                                Text(
                                    text = record.date.split(" ").getOrNull(1) ?: "",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = record.date,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "In: ${record.checkInTime}  •  Out: ${record.checkOutTime ?: "Currently Active"}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (record.status == "Present") StatusSuccess.copy(alpha = 0.15f) else BrandPrimary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = record.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (record.status == "Present") StatusSuccess else BrandPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${record.totalHours} hrs",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

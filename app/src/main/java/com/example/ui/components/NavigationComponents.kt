package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.User
import com.example.model.UserRole
import com.example.ui.theme.BrandPrimary
import com.example.viewmodel.AppScreen

data class NavigationNavItem(
    val screen: AppScreen,
    val icon: ImageVector,
    val label: String,
    val badgeCount: Int? = null,
    val minRole: UserRole? = null
)

object NavConfig {
    val employeeNavItems = listOf(
        NavigationNavItem(AppScreen.EMPLOYEE_DASHBOARD, Icons.Default.Dashboard, "Dashboard"),
        NavigationNavItem(AppScreen.TASK_MANAGEMENT, Icons.Default.Assignment, "Tasks & Board"),
        NavigationNavItem(AppScreen.PROJECTS, Icons.Default.Folder, "Projects"),
        NavigationNavItem(AppScreen.WORKFLOW_MANAGEMENT, Icons.Default.AccountTree, "Workflows"),
        NavigationNavItem(AppScreen.AI_ASSISTANT, Icons.Default.SmartToy, "AI Assistant"),
        NavigationNavItem(AppScreen.PERFORMANCE, Icons.Default.TrendingUp, "Productivity"),
        NavigationNavItem(AppScreen.ATTENDANCE, Icons.Default.AccessTime, "Attendance"),
        NavigationNavItem(AppScreen.AI_INSIGHTS, Icons.Default.Lightbulb, "AI Insights"),
        NavigationNavItem(AppScreen.REPORTS_ANALYTICS, Icons.Default.BarChart, "Analytics"),
        NavigationNavItem(AppScreen.NOTIFICATIONS, Icons.Default.Notifications, "Notifications", badgeCount = 2),
        NavigationNavItem(AppScreen.SETTINGS, Icons.Default.Settings, "Settings")
    )

    val managerNavItems = listOf(
        NavigationNavItem(AppScreen.MANAGER_DASHBOARD, Icons.Default.Speed, "Overview"),
        NavigationNavItem(AppScreen.TASK_MANAGEMENT, Icons.Default.Assignment, "Team Tasks"),
        NavigationNavItem(AppScreen.PERFORMANCE, Icons.Default.Group, "Team Performance"),
        NavigationNavItem(AppScreen.WORKFLOW_MANAGEMENT, Icons.Default.AccountTree, "Workflows"),
        NavigationNavItem(AppScreen.PROJECTS, Icons.Default.Folder, "Projects"),
        NavigationNavItem(AppScreen.AI_ASSISTANT, Icons.Default.SmartToy, "AI Assistant"),
        NavigationNavItem(AppScreen.REPORTS_ANALYTICS, Icons.Default.BarChart, "Reports"),
        NavigationNavItem(AppScreen.SETTINGS, Icons.Default.Settings, "Settings")
    )

    val adminNavItems = listOf(
        NavigationNavItem(AppScreen.ADMIN_DASHBOARD, Icons.Default.AdminPanelSettings, "Admin Overview"),
        NavigationNavItem(AppScreen.USER_MANAGEMENT, Icons.Default.PeopleAlt, "Users & Roles"),
        NavigationNavItem(AppScreen.SYSTEM_LOGS, Icons.Default.ReceiptLong, "System Logs"),
        NavigationNavItem(AppScreen.WORKFLOW_MANAGEMENT, Icons.Default.AccountTree, "Workflows"),
        NavigationNavItem(AppScreen.REPORTS_ANALYTICS, Icons.Default.Assessment, "Audit Reports"),
        NavigationNavItem(AppScreen.SETTINGS, Icons.Default.Settings, "Settings")
    )
}

@Composable
fun AppSidebar(
    currentUser: User?,
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onLogout: () -> Unit,
    onSwitchRole: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    val role = currentUser?.role ?: UserRole.EMPLOYEE
    val navItems = when (role) {
        UserRole.EMPLOYEE -> NavConfig.employeeNavItems
        UserRole.MANAGER -> NavConfig.managerNavItems
        UserRole.ADMINISTRATOR -> NavConfig.adminNavItems
    }

    Surface(
        modifier = modifier.fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxHeight()
        ) {
            // App Branding Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BrandPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Workspaces,
                        contentDescription = "WorkFlow AI Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = "WorkFlow AI",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Enterprise Suite",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User Info pill with quick role toggle
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(currentUser?.avatarColorHex ?: 0xFF3B82F6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser?.initials ?: "U",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentUser?.name ?: "User",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                            Text(
                                text = currentUser?.role?.displayName ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Demo Switch Role row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        UserRole.values().forEach { r ->
                            val isCurrent = r == role
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isCurrent) BrandPrimary else Color.Transparent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onSwitchRole(r) }
                            ) {
                                Text(
                                    text = when (r) {
                                        UserRole.EMPLOYEE -> "Emp"
                                        UserRole.MANAGER -> "Mgr"
                                        UserRole.ADMINISTRATOR -> "Adm"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(8.dp))

            // Navigation items list
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                navItems.forEach { item ->
                    val selected = currentScreen == item.screen
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (selected) BrandPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        },
                        badge = {
                            if (item.badgeCount != null) {
                                Badge(containerColor = BrandPrimary) {
                                    Text("${item.badgeCount}", color = Color.White)
                                }
                            }
                        },
                        selected = selected,
                        onClick = { onNavigate(item.screen) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("nav_item_${item.screen.name.lowercase()}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = BrandPrimary.copy(alpha = 0.12f),
                            selectedTextColor = BrandPrimary
                        )
                    )
                }
            }

            // Bottom logout
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onLogout() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Log Out",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Log Out",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    isDarkMode: Boolean,
    onToggleDark: () -> Unit,
    onOpenNav: () -> Unit,
    unreadNotificationCount: Int,
    onOpenNotifications: () -> Unit,
    onOpenAiChat: () -> Unit,
    showMenuButton: Boolean = true
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Work Smarter. Manage Better. Perform Better.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 1
                )
            }
        },
        navigationIcon = {
            if (showMenuButton) {
                IconButton(onClick = onOpenNav) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu Navigation")
                }
            }
        },
        actions = {
            // AI Assistant shortcut
            IconButton(onClick = onOpenAiChat) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "AI Assistant",
                    tint = BrandPrimary
                )
            }

            // Notification Bell with Badge
            IconButton(onClick = onOpenNotifications) {
                BadgedBox(badge = {
                    if (unreadNotificationCount > 0) {
                        Badge { Text("$unreadNotificationCount") }
                    }
                }) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                }
            }

            // Dark Mode toggle
            IconButton(onClick = onToggleDark) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

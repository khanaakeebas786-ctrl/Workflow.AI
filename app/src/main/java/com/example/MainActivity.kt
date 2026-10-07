package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.example.model.UserRole
import com.example.ui.components.AppSidebar
import com.example.ui.components.AppTopBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(uiState.userNotice) {
                uiState.userNotice?.let { notice ->
                    snackbarHostState.showSnackbar(notice)
                    viewModel.clearNotice()
                }
            }

            MyApplicationTheme(darkTheme = uiState.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!uiState.isAuthenticated || uiState.currentScreen == AppScreen.LANDING_AUTH) {
                        // Auth Portal
                        AuthPortalScreen(
                            onLoginSuccess = { role -> viewModel.loginAs(role) }
                        )
                    } else {
                        // Main Enterprise Dashboard with Responsive Sidebar + TopBar
                        MainWorkspaceContent(
                            viewModel = viewModel,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainWorkspaceContent(
    viewModel: MainViewModel,
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 840.dp

        if (isWideScreen) {
            // Tablet / Desktop Wide Layout: Permanent Sidebar on Left + Content on Right
            Row(modifier = Modifier.fillMaxSize()) {
                AppSidebar(
                    currentUser = uiState.currentUser,
                    currentScreen = uiState.currentScreen,
                    onNavigate = { viewModel.navigateTo(it) },
                    onLogout = { viewModel.logout() },
                    onSwitchRole = { viewModel.loginAs(it) },
                    modifier = Modifier.width(260.dp)
                )

                Scaffold(
                    modifier = Modifier.weight(1f),
                    topBar = {
                        AppTopBar(
                            title = uiState.currentScreen.title,
                            isDarkMode = uiState.isDarkMode,
                            onToggleDark = { viewModel.toggleDarkMode() },
                            onOpenNav = { },
                            unreadNotificationCount = uiState.notifications.count { !it.isRead },
                            onOpenNotifications = { viewModel.navigateTo(AppScreen.NOTIFICATIONS) },
                            onOpenAiChat = { viewModel.navigateTo(AppScreen.AI_ASSISTANT) },
                            showMenuButton = false
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ActiveScreenRouter(viewModel = viewModel)
                    }
                }
            }
        } else {
            // Mobile Layout: Modal Navigation Drawer + Responsive TopBar
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        modifier = Modifier.width(300.dp),
                        drawerContainerColor = MaterialTheme.colorScheme.surface
                    ) {
                        AppSidebar(
                            currentUser = uiState.currentUser,
                            currentScreen = uiState.currentScreen,
                            onNavigate = {
                                viewModel.navigateTo(it)
                                coroutineScope.launch { drawerState.close() }
                            },
                            onLogout = { viewModel.logout() },
                            onSwitchRole = {
                                viewModel.loginAs(it)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = uiState.currentScreen.title,
                            isDarkMode = uiState.isDarkMode,
                            onToggleDark = { viewModel.toggleDarkMode() },
                            onOpenNav = {
                                coroutineScope.launch { drawerState.open() }
                            },
                            unreadNotificationCount = uiState.notifications.count { !it.isRead },
                            onOpenNotifications = { viewModel.navigateTo(AppScreen.NOTIFICATIONS) },
                            onOpenAiChat = { viewModel.navigateTo(AppScreen.AI_ASSISTANT) },
                            showMenuButton = true
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ActiveScreenRouter(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveScreenRouter(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.currentScreen) {
        AppScreen.EMPLOYEE_DASHBOARD -> {
            EmployeeDashboardScreen(
                state = uiState,
                onNavigate = { viewModel.navigateTo(it) },
                onUpdateTaskStatus = { id, status -> viewModel.updateTaskStatus(id, status) },
                onToggleCheckIn = { viewModel.toggleCheckIn() }
            )
        }
        AppScreen.MANAGER_DASHBOARD -> {
            ManagerDashboardScreen(
                state = uiState,
                onNavigate = { viewModel.navigateTo(it) },
                onReassignTask = { taskId, newAssignee -> viewModel.reassignTask(taskId, newAssignee) }
            )
        }
        AppScreen.ADMIN_DASHBOARD -> {
            AdminDashboardScreen(
                state = uiState,
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
        AppScreen.TASK_MANAGEMENT -> {
            TaskManagementScreen(
                tasks = uiState.tasks,
                searchQuery = uiState.taskSearchQuery,
                filterStatus = uiState.taskFilterStatus,
                filterPriority = uiState.taskFilterPriority,
                onSearchChange = { viewModel.setTaskSearchQuery(it) },
                onFilterStatusChange = { viewModel.filterTaskByStatus(it) },
                onFilterPriorityChange = { viewModel.filterTaskByPriority(it) },
                onUpdateTaskStatus = { id, status -> viewModel.updateTaskStatus(id, status) },
                onDeleteTask = { id -> viewModel.deleteTask(id) },
                onAddTask = { title, desc, prio, cat, deadline, proj ->
                    viewModel.addTask(title, desc, prio, cat, deadline, proj)
                }
            )
        }
        AppScreen.WORKFLOW_MANAGEMENT -> {
            WorkflowManagementScreen(
                workflows = uiState.workflows,
                onAdvanceStage = { viewModel.advanceWorkflowStage(it) }
            )
        }
        AppScreen.AI_ASSISTANT -> {
            AiAssistantScreen(
                chatMessages = uiState.chatMessages,
                isAiThinking = uiState.isAiThinking,
                onSendMessage = { viewModel.sendUserChatMessage(it) }
            )
        }
        AppScreen.PERFORMANCE -> {
            PerformanceScreen(
                performanceList = uiState.teamPerformance
            )
        }
        AppScreen.ATTENDANCE -> {
            AttendanceScreen(
                attendanceList = uiState.attendance,
                isCheckedIn = uiState.isCheckedIn,
                todayHoursWorked = uiState.todayHoursWorked,
                onToggleCheckIn = { viewModel.toggleCheckIn() }
            )
        }
        AppScreen.PROJECTS -> {
            ProjectsScreen(
                projects = uiState.projects,
                onAddProject = { name, dept, deadline -> viewModel.addProject(name, dept, deadline) }
            )
        }
        AppScreen.REPORTS_ANALYTICS -> {
            ReportsAnalyticsScreen(
                state = uiState,
                onExportCsv = { viewModel.showNotice("Export generated: WorkFlow_Analytics_Q4.csv downloaded") }
            )
        }
        AppScreen.AI_INSIGHTS -> {
            AiInsightsScreen(
                insights = uiState.aiInsights,
                onApplyInsight = { viewModel.showNotice("AI optimization proposal queued for review") }
            )
        }
        AppScreen.NOTIFICATIONS -> {
            NotificationsScreen(
                notifications = uiState.notifications,
                onMarkAsRead = { viewModel.markNotificationAsRead(it) },
                onMarkAllRead = { viewModel.markAllNotificationsRead() }
            )
        }
        AppScreen.SETTINGS -> {
            SettingsScreen(
                currentUser = uiState.currentUser,
                isDarkMode = uiState.isDarkMode,
                onToggleDarkMode = { viewModel.toggleDarkMode() }
            )
        }
        AppScreen.USER_MANAGEMENT -> {
            UserManagementScreen(
                users = com.example.data.MockDataProvider.sampleUsers,
                onAddUser = { viewModel.showNotice("User added to directory") }
            )
        }
        AppScreen.SYSTEM_LOGS -> {
            SystemLogsScreen(
                logs = uiState.systemLogs
            )
        }
        AppScreen.LANDING_AUTH -> {
            AuthPortalScreen(onLoginSuccess = { viewModel.loginAs(it) })
        }
    }
}

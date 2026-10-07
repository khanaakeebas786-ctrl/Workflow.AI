package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockDataProvider
import com.example.model.*
import com.example.service.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class AppScreen(val title: String) {
    LANDING_AUTH("Sign In / Portal"),
    EMPLOYEE_DASHBOARD("Employee Dashboard"),
    MANAGER_DASHBOARD("Manager Dashboard"),
    ADMIN_DASHBOARD("Administrator Dashboard"),
    TASK_MANAGEMENT("Tasks & Kanban"),
    WORKFLOW_MANAGEMENT("Workflows"),
    AI_ASSISTANT("AI Assistant"),
    PERFORMANCE("Performance & Metrics"),
    ATTENDANCE("Attendance & Work Log"),
    PROJECTS("Projects"),
    REPORTS_ANALYTICS("Analytics & Reports"),
    AI_INSIGHTS("AI Insights"),
    NOTIFICATIONS("Notifications"),
    SETTINGS("Settings"),
    USER_MANAGEMENT("User Directory"),
    SYSTEM_LOGS("System Audit Logs")
}

data class AppUiState(
    val currentUser: User? = null,
    val isAuthenticated: Boolean = false,
    val currentScreen: AppScreen = AppScreen.LANDING_AUTH,
    val isDarkMode: Boolean = false,
    
    // Core Collections
    val tasks: List<TaskItem> = emptyList(),
    val projects: List<ProjectItem> = emptyList(),
    val workflows: List<WorkflowItem> = emptyList(),
    val attendance: List<AttendanceRecord> = emptyList(),
    val teamPerformance: List<EmployeePerformance> = emptyList(),
    val aiInsights: List<AIInsight> = emptyList(),
    val notifications: List<NotificationItem> = emptyList(),
    val departments: List<DepartmentItem> = emptyList(),
    val systemLogs: List<SystemLogItem> = emptyList(),
    
    // AI Chat
    val chatMessages: List<ChatMessage> = emptyList(),
    val isAiThinking: Boolean = false,
    
    // Attendance State
    val isCheckedIn: Boolean = true,
    val todayHoursWorked: Double = 5.8,
    
    // Search and Filters
    val taskSearchQuery: String = "",
    val taskFilterStatus: TaskStatus? = null,
    val taskFilterPriority: TaskPriority? = null,
    
    // Snackbar or Toast
    val userNotice: String? = null
)

class MainViewModel : ViewModel() {

    private val geminiService = GeminiService()

    private val _uiState = MutableStateFlow(
        AppUiState(
            currentUser = MockDataProvider.sampleUsers[0], // Employee by default
            isAuthenticated = true, // demo ready right on launch
            currentScreen = AppScreen.EMPLOYEE_DASHBOARD,
            tasks = MockDataProvider.getInitialTasks(),
            projects = MockDataProvider.getInitialProjects(),
            workflows = MockDataProvider.getInitialWorkflows(),
            attendance = MockDataProvider.getInitialAttendance(),
            teamPerformance = MockDataProvider.getInitialTeamPerformance(),
            aiInsights = MockDataProvider.getInitialAIInsights(),
            notifications = MockDataProvider.getInitialNotifications(),
            departments = MockDataProvider.getInitialDepartments(),
            systemLogs = MockDataProvider.getInitialSystemLogs(),
            chatMessages = listOf(
                ChatMessage(
                    id = "c1",
                    isFromUser = false,
                    message = "Hello Alex! I am your WorkFlow AI Assistant. I have analyzed your sprint backlog and attendance hours. You have 2 high-priority tasks due today, and team productivity is trending +18%. How can I help optimize your schedule today?",
                    timestamp = "09:05 AM"
                )
            )
        )
    )
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    fun toggleDarkMode() {
        _uiState.value = _uiState.value.copy(isDarkMode = !_uiState.value.isDarkMode)
    }

    fun clearNotice() {
        _uiState.value = _uiState.value.copy(userNotice = null)
    }

    fun showNotice(message: String) {
        _uiState.value = _uiState.value.copy(userNotice = message)
    }

    // Role switching & authentication simulation
    fun loginAs(role: UserRole) {
        val user = MockDataProvider.sampleUsers.firstOrNull { it.role == role }
            ?: MockDataProvider.sampleUsers[0]
        
        val defaultScreen = when (role) {
            UserRole.EMPLOYEE -> AppScreen.EMPLOYEE_DASHBOARD
            UserRole.MANAGER -> AppScreen.MANAGER_DASHBOARD
            UserRole.ADMINISTRATOR -> AppScreen.ADMIN_DASHBOARD
        }

        _uiState.value = _uiState.value.copy(
            currentUser = user,
            isAuthenticated = true,
            currentScreen = defaultScreen,
            userNotice = "Signed in as ${user.name} (${role.displayName})"
        )
    }

    fun logout() {
        _uiState.value = _uiState.value.copy(
            isAuthenticated = false,
            currentScreen = AppScreen.LANDING_AUTH,
            userNotice = "Signed out successfully"
        )
    }

    // Task management interactions
    fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        val updated = _uiState.value.tasks.map { task ->
            if (task.id == taskId) {
                val newProgress = when (newStatus) {
                    TaskStatus.COMPLETED -> 100
                    TaskStatus.IN_PROGRESS -> if (task.progress == 0) 50 else task.progress
                    TaskStatus.TODO -> 0
                    TaskStatus.OVERDUE -> task.progress
                }
                task.copy(status = newStatus, progress = newProgress)
            } else task
        }
        _uiState.value = _uiState.value.copy(tasks = updated, userNotice = "Task status updated to ${newStatus.displayName}")
    }

    fun addTask(
        title: String,
        description: String,
        priority: TaskPriority,
        category: String,
        deadline: String,
        projectName: String
    ) {
        val newTask = TaskItem(
            id = "t_${System.currentTimeMillis()}",
            title = title,
            description = description,
            status = TaskStatus.TODO,
            priority = priority,
            category = category.ifBlank { "General" },
            assignedToId = _uiState.value.currentUser?.id ?: "u1",
            assignedToName = _uiState.value.currentUser?.name ?: "Alex Morgan",
            projectId = "p1",
            projectName = projectName.ifBlank { "General Deliverables" },
            deadline = deadline.ifBlank { "Tomorrow" },
            progress = 0,
            estimatedHours = 4.0,
            spentHours = 0.0
        )
        _uiState.value = _uiState.value.copy(
            tasks = listOf(newTask) + _uiState.value.tasks,
            userNotice = "Task created successfully"
        )
    }

    fun deleteTask(taskId: String) {
        _uiState.value = _uiState.value.copy(
            tasks = _uiState.value.tasks.filterNot { it.id == taskId },
            userNotice = "Task removed"
        )
    }

    fun setTaskSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(taskSearchQuery = query)
    }

    fun filterTaskByStatus(status: TaskStatus?) {
        _uiState.value = _uiState.value.copy(taskFilterStatus = status)
    }

    fun filterTaskByPriority(priority: TaskPriority?) {
        _uiState.value = _uiState.value.copy(taskFilterPriority = priority)
    }

    // Attendance Actions
    fun toggleCheckIn() {
        val now = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val today = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date())

        if (_uiState.value.isCheckedIn) {
            // Check out
            val updatedRecords = _uiState.value.attendance.mapIndexed { idx, rec ->
                if (idx == 0) rec.copy(checkOutTime = now, status = "Completed", totalHours = 8.2) else rec
            }
            _uiState.value = _uiState.value.copy(
                isCheckedIn = false,
                attendance = updatedRecords,
                userNotice = "Checked out at $now. Today's hours recorded."
            )
        } else {
            // Check in
            val newRecord = AttendanceRecord(
                id = "att_${System.currentTimeMillis()}",
                date = "Today ($today)",
                dayOfWeek = "Active",
                checkInTime = now,
                checkOutTime = null,
                totalHours = 0.1,
                status = "Present"
            )
            _uiState.value = _uiState.value.copy(
                isCheckedIn = true,
                attendance = listOf(newRecord) + _uiState.value.attendance,
                userNotice = "Checked in at $now. Have a productive day!"
            )
        }
    }

    // AI Chat Assistant
    fun sendUserChatMessage(prompt: String) {
        if (prompt.isBlank()) return
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val userMsg = ChatMessage(
            id = "c_${System.currentTimeMillis()}",
            isFromUser = true,
            message = prompt.trim(),
            timestamp = time
        )

        _uiState.value = _uiState.value.copy(
            chatMessages = _uiState.value.chatMessages + userMsg,
            isAiThinking = true
        )

        viewModelScope.launch {
            val user = _uiState.value.currentUser?.name ?: "Employee"
            val pendingCount = _uiState.value.tasks.count { it.status != TaskStatus.COMPLETED }
            val completedCount = _uiState.value.tasks.count { it.status == TaskStatus.COMPLETED }
            val context = "User: $user. Tasks: $completedCount completed, $pendingCount pending. Active Workflows: 2."

            val reply = geminiService.generateAssistantReply(prompt, context)
            val replyTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

            val botMsg = ChatMessage(
                id = "c_${System.currentTimeMillis() + 1}",
                isFromUser = false,
                message = reply,
                timestamp = replyTime
            )

            _uiState.value = _uiState.value.copy(
                chatMessages = _uiState.value.chatMessages + botMsg,
                isAiThinking = false
            )
        }
    }

    // Notifications
    fun markNotificationAsRead(id: String) {
        val updated = _uiState.value.notifications.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
        _uiState.value = _uiState.value.copy(notifications = updated)
    }

    fun markAllNotificationsRead() {
        val updated = _uiState.value.notifications.map { it.copy(isRead = true) }
        _uiState.value = _uiState.value.copy(notifications = updated, userNotice = "All notifications marked read")
    }

    // Workflow stage advance
    fun advanceWorkflowStage(workflowId: String) {
        val updated = _uiState.value.workflows.map { wf ->
            if (wf.id == workflowId) {
                val nextIndex = (wf.activeStageIndex + 1).coerceAtMost(wf.stages.size - 1)
                val updatedStages = wf.stages.mapIndexed { i, stage ->
                    if (i <= nextIndex) stage.copy(isCompleted = true) else stage
                }
                val isAllCompleted = nextIndex == wf.stages.size - 1
                wf.copy(
                    activeStageIndex = nextIndex,
                    stages = updatedStages,
                    status = if (isAllCompleted) WorkflowStatus.COMPLETED else WorkflowStatus.ACTIVE
                )
            } else wf
        }
        _uiState.value = _uiState.value.copy(workflows = updated, userNotice = "Workflow progressed to next stage")
    }

    // Reassign task
    fun reassignTask(taskId: String, newAssigneeName: String) {
        val updated = _uiState.value.tasks.map {
            if (it.id == taskId) it.copy(assignedToName = newAssigneeName) else it
        }
        _uiState.value = _uiState.value.copy(tasks = updated, userNotice = "Task reassigned to $newAssigneeName")
    }

    // Add Project
    fun addProject(name: String, dept: String, deadline: String) {
        val newProj = ProjectItem(
            id = "p_${System.currentTimeMillis()}",
            name = name,
            description = "High-priority enterprise project delivering strategic value.",
            department = dept,
            status = "Active",
            progress = 10,
            deadline = deadline.ifBlank { "Next Month" },
            teamMemberCount = 4,
            totalTasks = 6,
            completedTasks = 1
        )
        _uiState.value = _uiState.value.copy(
            projects = listOf(newProj) + _uiState.value.projects,
            userNotice = "Project '$name' initiated"
        )
    }
}

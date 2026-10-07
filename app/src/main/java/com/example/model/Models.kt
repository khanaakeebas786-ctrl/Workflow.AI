package com.example.model

enum class UserRole(val displayName: String) {
    EMPLOYEE("Employee"),
    MANAGER("Manager"),
    ADMINISTRATOR("Administrator")
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val department: String,
    val title: String,
    val avatarColorHex: Long = 0xFF3B82F6,
    val initials: String = "EM"
)

enum class TaskStatus(val displayName: String) {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    OVERDUE("Overdue")
}

enum class TaskPriority(val displayName: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical")
}

data class TaskItem(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val priority: TaskPriority,
    val category: String,
    val assignedToId: String,
    val assignedToName: String,
    val projectId: String,
    val projectName: String,
    val deadline: String,
    val progress: Int = 0,
    val estimatedHours: Double = 4.0,
    val spentHours: Double = 2.0
)

data class ProjectItem(
    val id: String,
    val name: String,
    val description: String,
    val department: String,
    val status: String, // Active, In Review, Planning, Completed
    val progress: Int,
    val deadline: String,
    val teamMemberCount: Int,
    val totalTasks: Int,
    val completedTasks: Int
)

data class WorkflowStage(
    val id: String,
    val name: String,
    val assignedRole: String,
    val order: Int,
    val isCompleted: Boolean = false
)

enum class WorkflowStatus(val displayName: String) {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    DELAYED("Delayed")
}

data class WorkflowItem(
    val id: String,
    val title: String,
    val department: String,
    val status: WorkflowStatus,
    val efficiencyScore: Int, // e.g. 92%
    val avgCompletionTimeHours: Double,
    val bottleneckStage: String?,
    val stages: List<WorkflowStage>,
    val activeStageIndex: Int
)

data class AttendanceRecord(
    val id: String,
    val date: String,
    val dayOfWeek: String,
    val checkInTime: String,
    val checkOutTime: String?,
    val totalHours: Double,
    val status: String // "On Time", "Late", "Present", "Completed"
)

data class EmployeePerformance(
    val employeeId: String,
    val name: String,
    val department: String,
    val role: String,
    val productivityScore: Int,
    val tasksCompleted: Int,
    val onTimeCompletionRate: Int,
    val avgTaskHours: Double,
    val attendanceRate: Int,
    val workloadLevel: String, // "Optimal", "High", "Light"
    val needsSupport: Boolean = false
)

data class AIInsight(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // "Trend", "Workload", "Warning", "Efficiency"
    val timestamp: String,
    val actionSuggested: String
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: String, // "Task", "Deadline", "Workflow", "Announcement", "AI"
    val timeAgo: String,
    val isRead: Boolean = false
)

data class DepartmentItem(
    val id: String,
    val name: String,
    val managerName: String,
    val employeeCount: Int,
    val activeProjects: Int,
    val avgProductivity: Int
)

data class SystemLogItem(
    val id: String,
    val timestamp: String,
    val action: String,
    val userEmail: String,
    val status: String,
    val ipAddress: String
)

data class ChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val message: String,
    val timestamp: String,
    val quickActionPayload: String? = null
)

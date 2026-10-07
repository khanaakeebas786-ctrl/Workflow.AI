package com.example.data

import com.example.model.*

object MockDataProvider {

    val sampleUsers = listOf(
        User(
            id = "u1",
            name = "Alex Morgan",
            email = "alex.morgan@workflow.corp",
            role = UserRole.EMPLOYEE,
            department = "Software Engineering",
            title = "Senior Mobile Engineer",
            avatarColorHex = 0xFF4F46E5,
            initials = "AM"
        ),
        User(
            id = "u2",
            name = "Sarah Jenkins",
            email = "sarah.jenkins@workflow.corp",
            role = UserRole.MANAGER,
            department = "Software Engineering",
            title = "Engineering Director",
            avatarColorHex = 0xFF0EA5E9,
            initials = "SJ"
        ),
        User(
            id = "u3",
            name = "David Chen",
            email = "admin@workflow.corp",
            role = UserRole.ADMINISTRATOR,
            department = "Operations & Security",
            title = "System Administrator",
            avatarColorHex = 0xFF8B5CF6,
            initials = "DC"
        ),
        User(
            id = "u4",
            name = "Elena Rostova",
            email = "elena.r@workflow.corp",
            role = UserRole.EMPLOYEE,
            department = "Product Design",
            title = "Lead UI/UX Designer",
            avatarColorHex = 0xFFEC4899,
            initials = "ER"
        ),
        User(
            id = "u5",
            name = "Marcus Vance",
            email = "marcus.v@workflow.corp",
            role = UserRole.EMPLOYEE,
            department = "DevOps & Cloud",
            title = "Cloud Infrastructure Architect",
            avatarColorHex = 0xFF10B981,
            initials = "MV"
        )
    )

    fun getInitialTasks(): List<TaskItem> = listOf(
        TaskItem(
            id = "t1",
            title = "Implement OAuth Security Token Refresh",
            description = "Audit token expiry lifetime and implement background biometric refresh mechanisms.",
            status = TaskStatus.IN_PROGRESS,
            priority = TaskPriority.CRITICAL,
            category = "Security",
            assignedToId = "u1",
            assignedToName = "Alex Morgan",
            projectId = "p1",
            projectName = "Security & Auth Hardening",
            deadline = "Today, 5:00 PM",
            progress = 75,
            estimatedHours = 6.0,
            spentHours = 4.5
        ),
        TaskItem(
            id = "t2",
            title = "Refactor Database Indexing & Query Optimizations",
            description = "Optimize slow Room/SQLite queries on the attendance log table.",
            status = TaskStatus.TODO,
            priority = TaskPriority.HIGH,
            category = "Database",
            assignedToId = "u1",
            assignedToName = "Alex Morgan",
            projectId = "p2",
            projectName = "Core System Performance",
            deadline = "Tomorrow, 2:00 PM",
            progress = 20,
            estimatedHours = 5.0,
            spentHours = 1.0
        ),
        TaskItem(
            id = "t3",
            title = "Design High-Fidelity Tablet Supporting Pane",
            description = "Create responsive two-pane dashboard specifications for managers.",
            status = TaskStatus.COMPLETED,
            priority = TaskPriority.MEDIUM,
            category = "UI/UX",
            assignedToId = "u4",
            assignedToName = "Elena Rostova",
            projectId = "p3",
            projectName = "Q4 Enterprise Portal UX",
            deadline = "Yesterday",
            progress = 100,
            estimatedHours = 8.0,
            spentHours = 7.5
        ),
        TaskItem(
            id = "t4",
            title = "CI/CD Pipeline Automated Test Suite",
            description = "Integrate Robolectric and lint checks in GitHub Actions workflow.",
            status = TaskStatus.COMPLETED,
            priority = TaskPriority.HIGH,
            category = "DevOps",
            assignedToId = "u5",
            assignedToName = "Marcus Vance",
            projectId = "p1",
            projectName = "Security & Auth Hardening",
            deadline = "Oct 4",
            progress = 100,
            estimatedHours = 12.0,
            spentHours = 11.0
        ),
        TaskItem(
            id = "t5",
            title = "Legacy API Gateway Deprecation Migration",
            description = "Migrate outdated v1 endpoints to REST v2 with backward compatibility.",
            status = TaskStatus.OVERDUE,
            priority = TaskPriority.HIGH,
            category = "Backend",
            assignedToId = "u1",
            assignedToName = "Alex Morgan",
            projectId = "p2",
            projectName = "Core System Performance",
            deadline = "Yesterday, 6:00 PM",
            progress = 85,
            estimatedHours = 10.0,
            spentHours = 9.5
        ),
        TaskItem(
            id = "t6",
            title = "Team Workload Balancing & Resource Reallocation",
            description = "Analyze engineering sprint capacity and adjust sprint targets.",
            status = TaskStatus.IN_PROGRESS,
            priority = TaskPriority.MEDIUM,
            category = "Management",
            assignedToId = "u2",
            assignedToName = "Sarah Jenkins",
            projectId = "p3",
            projectName = "Q4 Enterprise Portal UX",
            deadline = "Oct 9",
            progress = 50,
            estimatedHours = 4.0,
            spentHours = 2.0
        ),
        TaskItem(
            id = "t7",
            title = "System Vulnerability Assessment & Compliance Sign-off",
            description = "Complete SOC-2 compliance checklist for tenant isolation.",
            status = TaskStatus.TODO,
            priority = TaskPriority.CRITICAL,
            category = "Compliance",
            assignedToId = "u3",
            assignedToName = "David Chen",
            projectId = "p1",
            projectName = "Security & Auth Hardening",
            deadline = "Oct 12",
            progress = 0,
            estimatedHours = 16.0,
            spentHours = 0.0
        )
    )

    fun getInitialProjects(): List<ProjectItem> = listOf(
        ProjectItem(
            id = "p1",
            name = "Security & Auth Hardening",
            description = "Enterprise SSO, biometric token renewals, and SOC-2 data verification.",
            department = "Software Engineering",
            status = "Active",
            progress = 82,
            deadline = "Oct 24, 2026",
            teamMemberCount = 6,
            totalTasks = 18,
            completedTasks = 15
        ),
        ProjectItem(
            id = "p2",
            name = "Core System Performance",
            description = "Database index scaling, real-time sync latency cuts, and caching.",
            department = "DevOps & Cloud",
            status = "Active",
            progress = 65,
            deadline = "Nov 15, 2026",
            teamMemberCount = 4,
            totalTasks = 12,
            completedTasks = 8
        ),
        ProjectItem(
            id = "p3",
            name = "Q4 Enterprise Portal UX",
            description = "Complete design revamp with responsive M3 design and dark theme support.",
            department = "Product Design",
            status = "In Review",
            progress = 94,
            deadline = "Oct 18, 2026",
            teamMemberCount = 5,
            totalTasks = 20,
            completedTasks = 19
        ),
        ProjectItem(
            id = "p4",
            name = "AI Telemetry & Predictive Workflows",
            description = "Predictive delay warning models and smart task auto-routing.",
            department = "AI Research",
            status = "Planning",
            progress = 30,
            deadline = "Dec 05, 2026",
            teamMemberCount = 3,
            totalTasks = 10,
            completedTasks = 3
        )
    )

    fun getInitialWorkflows(): List<WorkflowItem> = listOf(
        WorkflowItem(
            id = "w1",
            title = "Feature Release & Deployment Pipeline",
            department = "Software Engineering",
            status = WorkflowStatus.ACTIVE,
            efficiencyScore = 91,
            avgCompletionTimeHours = 32.5,
            bottleneckStage = "QA Automated Regression",
            stages = listOf(
                WorkflowStage("s1", "Specification Review", "Product Lead", 1, true),
                WorkflowStage("s2", "Code Implementation", "Senior Engineer", 2, true),
                WorkflowStage("s3", "QA Automated Regression", "QA Automation", 3, false),
                WorkflowStage("s4", "Security Audit", "SecOps Lead", 4, false),
                WorkflowStage("s5", "Canary Production Deploy", "DevOps Engineer", 5, false)
            ),
            activeStageIndex = 2
        ),
        WorkflowItem(
            id = "w2",
            title = "Employee Onboarding & Credentialing",
            department = "Operations & Security",
            status = WorkflowStatus.COMPLETED,
            efficiencyScore = 98,
            avgCompletionTimeHours = 14.0,
            bottleneckStage = null,
            stages = listOf(
                WorkflowStage("s11", "Background Verification", "HR Admin", 1, true),
                WorkflowStage("s12", "Hardware & Key Provisioning", "IT Support", 2, true),
                WorkflowStage("s13", "Team Intro & Workspace Setup", "Team Manager", 3, true)
            ),
            activeStageIndex = 2
        ),
        WorkflowItem(
            id = "w3",
            title = "Vendor Compliance & Security Audit",
            department = "Legal & Security",
            status = WorkflowStatus.DELAYED,
            efficiencyScore = 68,
            avgCompletionTimeHours = 74.0,
            bottleneckStage = "External Legal Review",
            stages = listOf(
                WorkflowStage("s21", "Vendor Risk Intake", "SecOps Analyst", 1, true),
                WorkflowStage("s22", "External Legal Review", "Legal Counsel", 2, false),
                WorkflowStage("s23", "CISO Authorization", "CISO", 3, false)
            ),
            activeStageIndex = 1
        )
    )

    fun getInitialAttendance(): List<AttendanceRecord> = listOf(
        AttendanceRecord("a1", "Today (Oct 07)", "Wednesday", "09:02 AM", null, 5.8, "Present"),
        AttendanceRecord("a2", "Oct 06", "Tuesday", "08:55 AM", "05:32 PM", 8.6, "Completed"),
        AttendanceRecord("a3", "Oct 05", "Monday", "09:00 AM", "05:15 PM", 8.25, "Completed"),
        AttendanceRecord("a4", "Oct 02", "Friday", "08:48 AM", "05:05 PM", 8.28, "Completed"),
        AttendanceRecord("a5", "Oct 01", "Thursday", "09:15 AM", "05:45 PM", 8.5, "On Time"),
        AttendanceRecord("a6", "Sep 30", "Wednesday", "08:58 AM", "05:10 PM", 8.2, "Completed"),
        AttendanceRecord("a7", "Sep 29", "Tuesday", "09:05 AM", "05:20 PM", 8.25, "Completed")
    )

    fun getInitialTeamPerformance(): List<EmployeePerformance> = listOf(
        EmployeePerformance(
            employeeId = "u1",
            name = "Alex Morgan",
            department = "Software Engineering",
            role = "Senior Mobile Engineer",
            productivityScore = 94,
            tasksCompleted = 24,
            onTimeCompletionRate = 96,
            avgTaskHours = 4.2,
            attendanceRate = 99,
            workloadLevel = "Optimal",
            needsSupport = false
        ),
        EmployeePerformance(
            employeeId = "u4",
            name = "Elena Rostova",
            department = "Product Design",
            role = "Lead UI/UX Designer",
            productivityScore = 92,
            tasksCompleted = 21,
            onTimeCompletionRate = 95,
            avgTaskHours = 3.8,
            attendanceRate = 98,
            workloadLevel = "Optimal",
            needsSupport = false
        ),
        EmployeePerformance(
            employeeId = "u5",
            name = "Marcus Vance",
            department = "DevOps & Cloud",
            role = "Cloud Architect",
            productivityScore = 88,
            tasksCompleted = 19,
            onTimeCompletionRate = 89,
            avgTaskHours = 5.6,
            attendanceRate = 96,
            workloadLevel = "High",
            needsSupport = true
        ),
        EmployeePerformance(
            employeeId = "u6",
            name = "Jordan Lee",
            department = "QA & Testing",
            role = "QA Engineer",
            productivityScore = 81,
            tasksCompleted = 15,
            onTimeCompletionRate = 84,
            avgTaskHours = 6.2,
            attendanceRate = 92,
            workloadLevel = "High",
            needsSupport = true
        ),
        EmployeePerformance(
            employeeId = "u7",
            name = "Taylor Swiftness",
            department = "Data Analytics",
            role = "BI Specialist",
            productivityScore = 95,
            tasksCompleted = 28,
            onTimeCompletionRate = 98,
            avgTaskHours = 3.5,
            attendanceRate = 100,
            workloadLevel = "Optimal",
            needsSupport = false
        )
    )

    fun getInitialAIInsights(): List<AIInsight> = listOf(
        AIInsight(
            id = "ins1",
            title = "Team Output Growth Surge",
            description = "Your team completed 18% more story tasks this week compared to last sprint.",
            category = "Trend",
            timestamp = "2 hours ago",
            actionSuggested = "Maintain current sprint pace; avoid overload."
        ),
        AIInsight(
            id = "ins2",
            title = "Approaching Deadline Notice",
            description = "Three tasks are within 24 hours of their scheduled target.",
            category = "Warning",
            timestamp = "3 hours ago",
            actionSuggested = "Review blocking PRs or reassign sub-tasks."
        ),
        AIInsight(
            id = "ins3",
            title = "Uneven Workload Distribution Detected",
            description = "Cloud Infrastructure backlog has 42% higher ticket allocation than frontend team.",
            category = "Workload",
            timestamp = "5 hours ago",
            actionSuggested = "Consider redistributing pending DevOps integration tasks."
        ),
        AIInsight(
            id = "ins4",
            title = "Workflow Bottleneck Identified",
            description = "Workflow 'Vendor Compliance & Security Audit' averages 74h turnaround due to Legal Review.",
            category = "Efficiency",
            timestamp = "1 day ago",
            actionSuggested = "Implement pre-approved standard NDAs to shave 32 hours."
        )
    )

    fun getInitialNotifications(): List<NotificationItem> = listOf(
        NotificationItem(
            id = "n1",
            title = "Critical Task Assigned",
            message = "Sarah Jenkins assigned you 'Implement OAuth Security Token Refresh'.",
            type = "Task",
            timeAgo = "10m ago",
            isRead = false
        ),
        NotificationItem(
            id = "n2",
            title = "AI Schedule Recommendation",
            message = "WorkFlow AI generated your optimized deep-work schedule for today.",
            type = "AI",
            timeAgo = "45m ago",
            isRead = false
        ),
        NotificationItem(
            id = "n3",
            title = "Workflow Stage Hand-off",
            message = "'Feature Release Pipeline' transitioned to QA Automated Regression stage.",
            type = "Workflow",
            timeAgo = "2h ago",
            isRead = true
        ),
        NotificationItem(
            id = "n4",
            title = "Manager Announcement",
            message = "Quarterly Productivity & Wellness review is scheduled for Thursday 3:00 PM.",
            type = "Announcement",
            timeAgo = "5h ago",
            isRead = true
        )
    )

    fun getInitialDepartments(): List<DepartmentItem> = listOf(
        DepartmentItem("d1", "Software Engineering", "Sarah Jenkins", 18, 5, 93),
        DepartmentItem("d2", "Product Design", "Elena Rostova", 8, 3, 91),
        DepartmentItem("d3", "DevOps & Cloud", "Marcus Vance", 6, 4, 88),
        DepartmentItem("d4", "Operations & Security", "David Chen", 10, 2, 95),
        DepartmentItem("d5", "Data & AI Intelligence", "Taylor Vance", 7, 3, 94)
    )

    fun getInitialSystemLogs(): List<SystemLogItem> = listOf(
        SystemLogItem("l1", "Oct 07 09:02:14", "User Check-In (Present)", "alex.morgan@workflow.corp", "SUCCESS", "192.168.1.104"),
        SystemLogItem("l2", "Oct 07 08:58:30", "Role Permissions Updated", "admin@workflow.corp", "SUCCESS", "10.0.0.1"),
        SystemLogItem("l3", "Oct 07 08:30:11", "OAuth Secret Key Rotated", "admin@workflow.corp", "AUDITED", "10.0.0.1"),
        SystemLogItem("l4", "Oct 07 07:15:00", "Automated Daily Metric Rollup", "system.daemon@workflow.internal", "SUCCESS", "127.0.0.1"),
        SystemLogItem("l5", "Oct 06 17:32:00", "User Check-Out (Completed 8.6h)", "alex.morgan@workflow.corp", "SUCCESS", "192.168.1.104")
    )
}

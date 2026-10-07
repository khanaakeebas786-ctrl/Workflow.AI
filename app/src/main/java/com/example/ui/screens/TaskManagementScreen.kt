package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TaskItem
import com.example.model.TaskPriority
import com.example.model.TaskStatus
import com.example.ui.components.TaskPriorityBadge
import com.example.ui.components.TaskStatusBadge
import com.example.ui.theme.*

enum class TaskViewMode { KANBAN, LIST_VIEW }

@Composable
fun TaskManagementScreen(
    tasks: List<TaskItem>,
    searchQuery: String,
    filterStatus: TaskStatus?,
    filterPriority: TaskPriority?,
    onSearchChange: (String) -> Unit,
    onFilterStatusChange: (TaskStatus?) -> Unit,
    onFilterPriorityChange: (TaskPriority?) -> Unit,
    onUpdateTaskStatus: (String, TaskStatus) -> Unit,
    onDeleteTask: (String) -> Unit,
    onAddTask: (String, String, TaskPriority, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var viewMode by remember { mutableStateOf(TaskViewMode.KANBAN) }
    var showCreateDialog by remember { mutableStateOf(false) }

    // Filter tasks
    val filteredTasks = tasks.filter { task ->
        val matchesQuery = searchQuery.isBlank() ||
                task.title.contains(searchQuery, ignoreCase = true) ||
                task.description.contains(searchQuery, ignoreCase = true) ||
                task.category.contains(searchQuery, ignoreCase = true)

        val matchesStatus = filterStatus == null || task.status == filterStatus
        val matchesPriority = filterPriority == null || task.priority == filterPriority

        matchesQuery && matchesStatus && matchesPriority
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Task & Kanban Board",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${filteredTasks.size} tasks active in current sprint cycle",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // View Mode Switcher
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(modifier = Modifier.padding(2.dp)) {
                        IconButton(
                            onClick = { viewMode = TaskViewMode.KANBAN },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (viewMode == TaskViewMode.KANBAN) BrandPrimary else Color.Transparent)
                        ) {
                            Icon(
                                Icons.Default.ViewWeek,
                                contentDescription = "Kanban",
                                tint = if (viewMode == TaskViewMode.KANBAN) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(
                            onClick = { viewMode = TaskViewMode.LIST_VIEW },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (viewMode == TaskViewMode.LIST_VIEW) BrandPrimary else Color.Transparent)
                        ) {
                            Icon(
                                Icons.Default.FormatListBulleted,
                                contentDescription = "List View",
                                tint = if (viewMode == TaskViewMode.LIST_VIEW) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Button(
                    onClick = { showCreateDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    modifier = Modifier.testTag("create_task_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Task", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search & Filter controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search by title, category, project...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("task_search_field"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status filter chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = filterStatus == null,
                    onClick = { onFilterStatusChange(null) },
                    label = { Text("All Statuses") }
                )
            }
            items(TaskStatus.values()) { status ->
                FilterChip(
                    selected = filterStatus == status,
                    onClick = { onFilterStatusChange(if (filterStatus == status) null else status) },
                    label = { Text(status.displayName) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Display Content according to view mode
        if (viewMode == TaskViewMode.KANBAN) {
            KanbanBoardView(
                tasks = filteredTasks,
                onUpdateStatus = onUpdateTaskStatus,
                onDeleteTask = onDeleteTask,
                modifier = Modifier.weight(1f)
            )
        } else {
            TaskListView(
                tasks = filteredTasks,
                onUpdateStatus = onUpdateTaskStatus,
                onDeleteTask = onDeleteTask,
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (showCreateDialog) {
        CreateTaskDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { title, desc, prio, cat, deadline, proj ->
                onAddTask(title, desc, prio, cat, deadline, proj)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun KanbanBoardView(
    tasks: List<TaskItem>,
    onUpdateStatus: (String, TaskStatus) -> Unit,
    onDeleteTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val todoTasks = tasks.filter { it.status == TaskStatus.TODO }
    val inProgressTasks = tasks.filter { it.status == TaskStatus.IN_PROGRESS }
    val completedTasks = tasks.filter { it.status == TaskStatus.COMPLETED }

    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        KanbanColumn(
            title = "To Do",
            count = todoTasks.size,
            columnColor = Color(0xFF64748B),
            tasks = todoTasks,
            onUpdateStatus = onUpdateStatus,
            onDeleteTask = onDeleteTask,
            modifier = Modifier.weight(1f)
        )
        KanbanColumn(
            title = "In Progress",
            count = inProgressTasks.size,
            columnColor = BrandPrimary,
            tasks = inProgressTasks,
            onUpdateStatus = onUpdateStatus,
            onDeleteTask = onDeleteTask,
            modifier = Modifier.weight(1f)
        )
        KanbanColumn(
            title = "Completed",
            count = completedTasks.size,
            columnColor = StatusSuccess,
            tasks = completedTasks,
            onUpdateStatus = onUpdateStatus,
            onDeleteTask = onDeleteTask,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun KanbanColumn(
    title: String,
    count: Int,
    columnColor: Color,
    tasks: List<TaskItem>,
    onUpdateStatus: (String, TaskStatus) -> Unit,
    onDeleteTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Column Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(columnColor)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = columnColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "$count",
                        color = columnColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tasks) { task ->
                    KanbanTaskCard(
                        task = task,
                        onUpdateStatus = onUpdateStatus,
                        onDeleteTask = onDeleteTask
                    )
                }
            }
        }
    }
}

@Composable
fun KanbanTaskCard(
    task: TaskItem,
    onUpdateStatus: (String, TaskStatus) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TaskPriorityBadge(task.priority)
                IconButton(
                    onClick = { onDeleteTask(task.id) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = task.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = task.description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { task.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = when (task.status) {
                    TaskStatus.COMPLETED -> StatusSuccess
                    TaskStatus.IN_PROGRESS -> BrandPrimary
                    else -> Color.Gray
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Due: ${task.deadline}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                // Quick column mover
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (task.status != TaskStatus.TODO) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable {
                                    val prev = if (task.status == TaskStatus.COMPLETED) TaskStatus.IN_PROGRESS else TaskStatus.TODO
                                    onUpdateStatus(task.id, prev)
                                }
                        ) {
                            Text("←", fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    if (task.status != TaskStatus.COMPLETED) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = BrandPrimary.copy(alpha = 0.15f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable {
                                    val next = if (task.status == TaskStatus.TODO) TaskStatus.IN_PROGRESS else TaskStatus.COMPLETED
                                    onUpdateStatus(task.id, next)
                                }
                        ) {
                            Text("→", fontSize = 11.sp, color = BrandPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskListView(
    tasks: List<TaskItem>,
    onUpdateStatus: (String, TaskStatus) -> Unit,
    onDeleteTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(tasks) { task ->
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TaskPriorityBadge(task.priority)
                            TaskStatusBadge(task.status)
                            Text(
                                text = "•  ${task.category}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = task.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Assigned: ${task.assignedToName}  |  Project: ${task.projectName}  |  Deadline: ${task.deadline}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = {
                            val next = when (task.status) {
                                TaskStatus.TODO -> TaskStatus.IN_PROGRESS
                                TaskStatus.IN_PROGRESS -> TaskStatus.COMPLETED
                                else -> TaskStatus.TODO
                            }
                            onUpdateStatus(task.id, next)
                        }) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Toggle status", tint = BrandPrimary)
                        }
                        IconButton(onClick = { onDeleteTask(task.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateTaskDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, TaskPriority, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TaskPriority.HIGH) }
    var category by remember { mutableStateOf("Core Engineering") }
    var deadline by remember { mutableStateOf("Tomorrow, 5:00 PM") }
    var projectName by remember { mutableStateOf("Security & Auth Hardening") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Enterprise Task", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & Acceptance Criteria") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (e.g. Backend, UI/UX)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Deadline Target") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = projectName,
                    onValueChange = { projectName = it },
                    label = { Text("Project / Initiative") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreate(title, description, priority, category, deadline, projectName)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text("Create Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

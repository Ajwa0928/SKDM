package com.example.crescent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.crescent.model.Teacher
import com.example.crescent.ui.CrescentViewModel
import com.example.crescent.ui.components.TeacherDetailDialog
import com.example.ui.theme.*

@Composable
fun TeachersScreen(
    viewModel: CrescentViewModel,
    modifier: Modifier = Modifier
) {
    val teachers by viewModel.teachers.collectAsState()
    val selectedDept by viewModel.selectedTeacherDepartment.collectAsState()
    val activeTeacherDetail by viewModel.selectedTeacherForDetail.collectAsState()

    val departments = remember {
        listOf(
            "All",
            "Science & Pre-Medical",
            "Physics & Applied Sciences",
            "Biological Sciences",
            "Computer Science & IT",
            "Mathematics",
            "Humanities & Languages"
        )
    }

    val filteredTeachers = remember(teachers, selectedDept) {
        if (selectedDept == "All") teachers else teachers.filter { it.department.contains(selectedDept, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // Intro Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Faculty & Teacher Directory",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CrescentGreenPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Parents and students can consult directly with teachers regarding academic progress, subject queries, and examination preparation at Mirali campus.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Department filter chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(departments) { dept ->
                    FilterChip(
                        selected = selectedDept == dept,
                        onClick = { viewModel.setTeacherDepartment(dept) },
                        label = { Text(dept, fontSize = 11.sp) }
                    )
                }
            }
        }

        // List of Teachers
        items(filteredTeachers) { teacher ->
            TeacherDirectoryCard(
                teacher = teacher,
                onViewProfile = { viewModel.setSelectedTeacher(teacher) },
                onSendMessage = {
                    viewModel.setComposeRecipient(teacher.name, teacher.designation)
                }
            )
        }
    }

    // Detail dialog
    activeTeacherDetail?.let { teacher ->
        TeacherDetailDialog(
            teacher = teacher,
            onSendMessage = {
                viewModel.setSelectedTeacher(null)
                viewModel.setComposeRecipient(teacher.name, teacher.designation)
            },
            onDismiss = { viewModel.setSelectedTeacher(null) }
        )
    }
}

@Composable
private fun TeacherDirectoryCard(
    teacher: Teacher,
    onViewProfile: () -> Unit,
    onSendMessage: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(CrescentGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = teacher.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = teacher.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = teacher.designation,
                        style = MaterialTheme.typography.bodySmall,
                        color = CrescentGreenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = teacher.qualification,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = CrescentGoldContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "${teacher.experienceYears}y Exp",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF453400),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = CrescentGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Office Hours: ${teacher.officeHours}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewProfile,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Profile Details", fontSize = 12.sp)
                }

                Button(
                    onClick = onSendMessage,
                    colors = ButtonDefaults.buttonColors(containerColor = CrescentGreenPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("teacher_card_message_button_${teacher.id}"),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Send Message", fontSize = 12.sp)
                }
            }
        }
    }
}

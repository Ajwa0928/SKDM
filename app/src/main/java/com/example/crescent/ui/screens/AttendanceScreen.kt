package com.example.crescent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.window.Dialog
import com.example.crescent.model.AttendanceRecord
import com.example.crescent.model.AttendanceStatus
import com.example.crescent.ui.CrescentViewModel
import com.example.ui.theme.*

@Composable
fun AttendanceScreen(
    viewModel: CrescentViewModel,
    modifier: Modifier = Modifier
) {
    val student by viewModel.selectedStudent.collectAsState()
    val allRecords by viewModel.attendanceRecords.collectAsState()
    val activeFilter by viewModel.attendanceFilter.collectAsState()

    var showMarkDialog by remember { mutableStateOf(false) }

    val studentRecords = remember(allRecords, student?.id) {
        allRecords.filter { it.studentId == (student?.id ?: "std_101") }
    }

    val filteredRecords = remember(studentRecords, activeFilter) {
        if (activeFilter == null) studentRecords else studentRecords.filter { it.status == activeFilter }
    }

    val presentCount = studentRecords.count { it.status == AttendanceStatus.PRESENT }
    val lateCount = studentRecords.count { it.status == AttendanceStatus.LATE }
    val absentCount = studentRecords.count { it.status == AttendanceStatus.ABSENT }
    val leaveCount = studentRecords.count { it.status == AttendanceStatus.LEAVE }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 88.dp)
        ) {
            // Header Overview Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CrescentGreenPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Monthly Attendance Ratio",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${student?.fullName ?: "Student"} • ${student?.gradeClass}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFE2F5ED)
                                )
                            }
                            Surface(
                                color = CrescentGoldSecondary,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${student?.attendancePercentage ?: 94.2f}%",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF2C2200),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Linear progress indicator
                        LinearProgressIndicator(
                            progress = { ((student?.attendancePercentage ?: 94.2f) / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = CrescentGoldSecondary,
                            trackColor = Color(0x40FFFFFF)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Summary Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AttendanceBadge("Present", "$presentCount", Color(0xFFDCFCE7), StatusPresent)
                            AttendanceBadge("Late", "$lateCount", Color(0xFFFFEDD5), StatusLate)
                            AttendanceBadge("Absent", "$absentCount", Color(0xFFFEE2E2), StatusAbsent)
                            AttendanceBadge("Leave", "$leaveCount", Color(0xFFDBEAFE), StatusLeave)
                        }
                    }
                }
            }

            // Subject-wise percentage breakdown
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Subject-Wise Attendance",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        SubjectAttendanceBar("Chemistry (Prof. Abdul Qayyum)", 0.96f, "96%")
                        SubjectAttendanceBar("Physics (Assoc. Prof. Tariq)", 0.92f, "92%")
                        SubjectAttendanceBar("Biology (Madam Zulaikha)", 0.95f, "95%")
                        SubjectAttendanceBar("English (Prof. Aslam Pervez)", 0.90f, "90%")
                        SubjectAttendanceBar("Islamic Studies & Urdu", 0.94f, "94%")
                    }
                }
            }

            // Filter Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = activeFilter == null,
                        onClick = { viewModel.setAttendanceFilter(null) },
                        label = { Text("All (${studentRecords.size})", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = activeFilter == AttendanceStatus.PRESENT,
                        onClick = { viewModel.setAttendanceFilter(AttendanceStatus.PRESENT) },
                        label = { Text("Present ($presentCount)", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = activeFilter == AttendanceStatus.ABSENT,
                        onClick = { viewModel.setAttendanceFilter(AttendanceStatus.ABSENT) },
                        label = { Text("Absent ($absentCount)", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = activeFilter == AttendanceStatus.LEAVE,
                        onClick = { viewModel.setAttendanceFilter(AttendanceStatus.LEAVE) },
                        label = { Text("Leave ($leaveCount)", fontSize = 11.sp) }
                    )
                }
            }

            // Attendance Log List
            items(filteredRecords) { record ->
                AttendanceItemCard(record)
            }
        }

        // Floating Action Button to Mark Today's Attendance
        ExtendedFloatingActionButton(
            onClick = { showMarkDialog = true },
            icon = { Icon(Icons.Default.Check, contentDescription = null) },
            text = { Text("Mark Today") },
            containerColor = CrescentGreenPrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("mark_attendance_fab")
        )

        if (showMarkDialog) {
            MarkAttendanceDialog(
                onDismiss = { showMarkDialog = false },
                onConfirm = { status, subject, remarks ->
                    viewModel.markAttendance(status, subject, remarks)
                    showMarkDialog = false
                }
            )
        }
    }
}

@Composable
private fun AttendanceItemCard(record: AttendanceRecord) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon Indicator
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (record.status) {
                            AttendanceStatus.PRESENT -> Color(0xFFDCFCE7)
                            AttendanceStatus.LATE -> Color(0xFFFFEDD5)
                            AttendanceStatus.ABSENT -> Color(0xFFFEE2E2)
                            AttendanceStatus.LEAVE -> Color(0xFFDBEAFE)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (record.status) {
                        AttendanceStatus.PRESENT -> Icons.Default.Check
                        AttendanceStatus.LATE -> Icons.Default.Schedule
                        AttendanceStatus.ABSENT -> Icons.Default.Close
                        AttendanceStatus.LEAVE -> Icons.Default.Description
                    },
                    contentDescription = null,
                    tint = when (record.status) {
                        AttendanceStatus.PRESENT -> StatusPresent
                        AttendanceStatus.LATE -> StatusLate
                        AttendanceStatus.ABSENT -> StatusAbsent
                        AttendanceStatus.LEAVE -> StatusLeave
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = record.subject,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = when (record.status) {
                            AttendanceStatus.PRESENT -> Color(0xFFDCFCE7)
                            AttendanceStatus.LATE -> Color(0xFFFFEDD5)
                            AttendanceStatus.ABSENT -> Color(0xFFFEE2E2)
                            AttendanceStatus.LEAVE -> Color(0xFFDBEAFE)
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = record.status.name,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = when (record.status) {
                                AttendanceStatus.PRESENT -> StatusPresent
                                AttendanceStatus.LATE -> StatusLate
                                AttendanceStatus.ABSENT -> StatusAbsent
                                AttendanceStatus.LEAVE -> StatusLeave
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${record.date} • ${record.teacherName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (record.remarks.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = record.remarks,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SubjectAttendanceBar(subject: String, progress: Float, percentText: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(subject, style = MaterialTheme.typography.bodySmall)
            Text(percentText, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = CrescentGreenPrimary)
        }
        Spacer(modifier = Modifier.height(3.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = CrescentGreenPrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun AttendanceBadge(label: String, count: String, bgColor: Color, textColor: Color) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(text = count, fontWeight = FontWeight.Bold, color = textColor, fontSize = 14.sp)
            Text(text = label, fontSize = 10.sp, color = textColor)
        }
    }
}

@Composable
private fun MarkAttendanceDialog(
    onDismiss: () -> Unit,
    onConfirm: (AttendanceStatus, String, String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(AttendanceStatus.PRESENT) }
    var subject by remember { mutableStateOf("Organic Chemistry Practical") }
    var remarks by remember { mutableStateOf("Verified in class attendance") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Mark Today's Attendance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CrescentGreenPrimary
                )
                Text(
                    text = "Record or update student presence for today's session",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("Select Status:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AttendanceStatus.values().forEach { st ->
                        FilterChip(
                            selected = selectedStatus == st,
                            onClick = { selectedStatus = st },
                            label = { Text(st.name.take(4), fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject / Period") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Remarks") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(selectedStatus, subject, remarks) },
                        colors = ButtonDefaults.buttonColors(containerColor = CrescentGreenPrimary),
                        modifier = Modifier.testTag("confirm_mark_attendance_button")
                    ) {
                        Text("Save Record")
                    }
                }
            }
        }
    }
}

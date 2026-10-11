package com.example.crescent.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.crescent.model.*
import com.example.crescent.ui.CrescentViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrescentTopAppBar(
    viewModel: CrescentViewModel,
    modifier: Modifier = Modifier
) {
    val selectedStudent by viewModel.selectedStudent.collectAsState()
    val unreadCount by viewModel.unreadNotificationsCount.collectAsState()

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                // College Crest
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CrescentGreenPrimary)
                        .border(1.5.dp, CrescentGoldSecondary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_crescent_logo),
                        contentDescription = "Crescent College Emblem",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "The Crescent College",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Mirali, NWTD • Academic Portal",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        actions = {
            // Student Switcher Chip
            AssistChip(
                onClick = { viewModel.setShowStudentSwitcher(true) },
                label = {
                    Text(
                        text = selectedStudent?.fullName?.split(" ")?.firstOrNull() ?: "Student",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Select Student",
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier
                    .padding(end = 4.dp)
                    .testTag("student_switcher_chip")
            )

            // Notifications Bell with Badge
            BadgedBox(
                badge = {
                    if (unreadCount > 0) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ) {
                            Text(text = "$unreadCount")
                        }
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                IconButton(
                    onClick = { viewModel.setShowNotifications(true) },
                    modifier = Modifier.testTag("notification_bell_button")
                ) {
                    Icon(
                        imageVector = if (unreadCount > 0) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = "Alerts & Notifications",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    )
}

@Composable
fun StudentSwitcherDialog(
    viewModel: CrescentViewModel,
    onDismiss: () -> Unit
) {
    val students by viewModel.students.collectAsState()
    val selectedStudent by viewModel.selectedStudent.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = null,
                    tint = CrescentGreenPrimary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Select Student / Ward",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Switch between enrolled children or student records at The Crescent School & Degree College:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(students) { student ->
                        val isSelected = student.id == selectedStudent?.id
                        Card(
                            onClick = { viewModel.selectStudent(student) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) CrescentGreenContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CrescentGreenPrimary)) else null,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("select_student_${student.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) CrescentGreenPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = student.fullName.take(1),
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = student.fullName,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${student.rollNumber} • ${student.gradeClass}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = CrescentGreenPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}

@Composable
fun NotificationsDialog(
    viewModel: CrescentViewModel,
    onDismiss: () -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()
    val selectedFilter by viewModel.selectedNotificationFilter.collectAsState()

    val filteredList = remember(notifications, selectedFilter) {
        if (selectedFilter == null) notifications else notifications.filter { it.category == selectedFilter }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Notifications & Alerts",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Automated Exam & Fee Reminders",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action buttons: Trigger automated alert demos & Mark All Read
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = { viewModel.triggerExamNotificationDemo() },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Exam Alert", fontSize = 11.sp)
                    }

                    FilledTonalButton(
                        onClick = { viewModel.triggerFeeReminderDemo() },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Fee Alert", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = { viewModel.markAllNotificationsRead() },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Read All", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filter chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = selectedFilter == null,
                        onClick = { viewModel.setNotificationFilter(null) },
                        label = { Text("All", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = selectedFilter == NotificationCategory.EXAM,
                        onClick = { viewModel.setNotificationFilter(NotificationCategory.EXAM) },
                        label = { Text("Exams", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(12.dp)) }
                    )
                    FilterChip(
                        selected = selectedFilter == NotificationCategory.FEE,
                        onClick = { viewModel.setNotificationFilter(NotificationCategory.FEE) },
                        label = { Text("Fees", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, null, modifier = Modifier.size(12.dp)) }
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (filteredList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No notifications in this category",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(filteredList) { item ->
                            val isExam = item.category == NotificationCategory.EXAM
                            val isFee = item.category == NotificationCategory.FEE
                            Card(
                                onClick = { viewModel.markNotificationRead(item.id) },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (!item.isRead) {
                                        if (isExam) Color(0xFFF0FDF4) else if (isFee) Color(0xFFFFFBEB) else CrescentGreenContainer.copy(alpha = 0.5f)
                                    } else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                ),
                                border = if (!item.isRead) {
                                    CardDefaults.outlinedCardBorder().copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(
                                            if (isExam) CrescentGreenPrimary else if (isFee) CrescentGoldSecondary else MaterialTheme.colorScheme.outline
                                        )
                                    )
                                } else null,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = when (item.category) {
                                                    NotificationCategory.EXAM -> Icons.Default.EventNote
                                                    NotificationCategory.FEE -> Icons.Default.ReceiptLong
                                                    NotificationCategory.ATTENDANCE -> Icons.Default.Check
                                                    NotificationCategory.GENERAL -> Icons.Default.Campaign
                                                },
                                                contentDescription = null,
                                                tint = if (isExam) CrescentGreenPrimary else if (isFee) Color(0xFFB45309) else MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = item.priorityBadge ?: item.category.name,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isExam) CrescentGreenPrimary else if (isFee) Color(0xFFB45309) else MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        Text(
                                            text = item.timestamp,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = item.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    if (item.dueDateOrDate != null) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Surface(
                                            color = if (isFee) Color(0xFFFEF3C7) else CrescentGreenContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = item.dueDateOrDate,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isFee) Color(0xFF92400E) else CrescentGreenPrimary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeeChallanDialog(
    voucher: FeeVoucher,
    onPay: () -> Unit,
    onDismiss: () -> Unit
) {
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
                // Official Challan Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "THE CRESCENT SCHOOL & DEGREE COLLEGE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = CrescentGreenPrimary
                        )
                        Text(
                            text = "Mirali, North Waziristan Tribal District",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 10.dp))

                // Challan Meta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Voucher No:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(voucher.voucherId, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Due Date:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(voucher.dueDate, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Student Details
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Student: ${voucher.studentName}", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
                        Text("Roll No: ${voucher.rollNumber} • Month: ${voucher.monthYear}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Designated Bank: ${voucher.bankBranch}", style = MaterialTheme.typography.labelSmall, color = CrescentGreenPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dues breakdown
                Text("Fee Breakdown:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                FeeRow("Tuition Fee", "PKR ${voucher.tuitionFee}")
                if (voucher.examinationFee > 0) FeeRow("Examination & Board Dues", "PKR ${voucher.examinationFee}")
                FeeRow("Science Laboratory Charges", "PKR ${voucher.labCharges}")
                FeeRow("Library & IT Development", "PKR ${voucher.libraryCharges}")

                Divider(modifier = Modifier.padding(vertical = 6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Amount Payable:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Text("PKR ${voucher.totalAmount}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, color = CrescentGreenPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (voucher.isPaid) {
                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusPresent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("PAID ON ${voucher.paidDate ?: "OCT 2026"}", fontWeight = FontWeight.Bold, color = StatusPresent)
                        }
                    }
                } else {
                    Button(
                        onClick = onPay,
                        colors = ButtonDefaults.buttonColors(containerColor = CrescentGreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pay_voucher_confirm_button")
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Confirm / Simulate Voucher Payment")
                    }
                }
            }
        }
    }
}

@Composable
private fun FeeRow(label: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(amount, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun TeacherDetailDialog(
    teacher: Teacher,
    onSendMessage: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(CrescentGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = teacher.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = teacher.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = teacher.designation,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Department: ${teacher.department}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        Text("Qualification: ${teacher.qualification}", style = MaterialTheme.typography.bodySmall)
                        Text("Experience: ${teacher.experienceYears} Years", style = MaterialTheme.typography.bodySmall)
                        Text("Consultation / Office Hours: ${teacher.officeHours}", style = MaterialTheme.typography.bodySmall, color = CrescentGreenPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Subjects Taught:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    teacher.subjectsTaught.forEach { subj ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(subj, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("About Faculty:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = teacher.bio,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSendMessage,
                    colors = ButtonDefaults.buttonColors(containerColor = CrescentGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("teacher_detail_send_message_button")
                ) {
                    Icon(Icons.Default.Mail, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Send Message to Teacher")
                }
            }
        }
    }
}

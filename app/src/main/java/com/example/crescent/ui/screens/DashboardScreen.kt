package com.example.crescent.ui.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.crescent.model.NotificationCategory
import com.example.crescent.ui.CrescentScreen
import com.example.crescent.ui.CrescentViewModel
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: CrescentViewModel,
    modifier: Modifier = Modifier
) {
    val student by viewModel.selectedStudent.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    // Find urgent upcoming exam and fee reminder
    val upcomingExam = remember(notifications) {
        notifications.firstOrNull { it.category == NotificationCategory.EXAM && !it.isRead }
            ?: notifications.firstOrNull { it.category == NotificationCategory.EXAM }
    }
    val feeReminder = remember(notifications) {
        notifications.firstOrNull { it.category == NotificationCategory.FEE && !it.isRead }
            ?: notifications.firstOrNull { it.category == NotificationCategory.FEE }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // Hero College Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_crescent_campus),
                        contentDescription = "Crescent College Mirali Campus",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xCC062E24)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            color = CrescentGoldSecondary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "MIRALI • NORTH WAZIRISTAN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C2200),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "The Crescent School & Degree College",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Excellence in Education, Science & Character Building",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2F5ED)
                        )
                    }
                }
            }
        }

        // Student Profile Header Card
        item {
            student?.let { s ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(CrescentGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = s.fullName.take(1),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = s.fullName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${s.rollNumber} • ${s.gradeClass}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            AssistChip(
                                onClick = { viewModel.setShowStudentSwitcher(true) },
                                label = { Text("Switch", fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        // Quick stats row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatColumn("Attendance", "${s.attendancePercentage}%", StatusPresent)
                            StatColumn("Term Result", s.currentGpaOrGrade, CrescentGreenPrimary)
                            StatColumn("Class Rank", s.rankInClass, CrescentGoldSecondary)
                            StatColumn(
                                "Fee Dues",
                                if (s.feeDueAmount > 0) "PKR ${s.feeDueAmount}" else "Cleared",
                                if (s.feeDueAmount > 0) MaterialTheme.colorScheme.error else StatusPresent
                            )
                        }
                    }
                }
            }
        }

        // Automated Reminders Section (Exam & Fee Notifications)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Automated Alerts & Reminders",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Exam Alert Card
                if (upcomingExam != null) {
                    Card(
                        onClick = { viewModel.navigateTo(CrescentScreen.GRADES) },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CrescentGreenPrimary)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_upcoming_exam_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(CrescentGreenContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EventAvailable,
                                    contentDescription = null,
                                    tint = CrescentGreenPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = CrescentGreenPrimary,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "UPCOMING EXAM",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = upcomingExam.dueDateOrDate ?: "Oct 2026",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CrescentGreenPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = upcomingExam.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = upcomingExam.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CrescentGreenPrimary)
                        }
                    }
                }

                // Fee Reminder Card
                if (feeReminder != null) {
                    Card(
                        onClick = { viewModel.navigateTo(CrescentScreen.ADMISSIONS_AND_FEES) },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFD97706))),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dashboard_fee_reminder_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = null,
                                    tint = Color(0xFFB45309),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = Color(0xFFD97706),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "FEE PAYMENT REMINDER",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = feeReminder.dueDateOrDate ?: "Due: 15 Oct 2026",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB45309)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = feeReminder.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = feeReminder.message,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFB45309))
                        }
                    }
                }
            }
        }

        // Quick Feature Navigation Grid
        item {
            Text(
                text = "Academic & School Services",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureGridCard(
                        title = "Attendance",
                        subtitle = "Detailed logs & stats",
                        icon = Icons.Default.FactCheck,
                        accentColor = StatusPresent,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_card_attendance"
                    ) {
                        viewModel.navigateTo(CrescentScreen.ATTENDANCE)
                    }

                    FeatureGridCard(
                        title = "Academic Grades",
                        subtitle = "Term marks & reports",
                        icon = Icons.Default.Grade,
                        accentColor = CrescentGreenPrimary,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_card_grades"
                    ) {
                        viewModel.navigateTo(CrescentScreen.GRADES)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureGridCard(
                        title = "Faculty Profiles",
                        subtitle = "Teacher consultations",
                        icon = Icons.Default.School,
                        accentColor = CrescentTertiary,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_card_teachers"
                    ) {
                        viewModel.navigateTo(CrescentScreen.TEACHERS)
                    }

                    FeatureGridCard(
                        title = "Online Admission",
                        subtitle = "New registration form",
                        icon = Icons.Default.AppRegistration,
                        accentColor = CrescentGoldSecondary,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_card_admission"
                    ) {
                        viewModel.navigateTo(CrescentScreen.ADMISSIONS_AND_FEES)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureGridCard(
                        title = "Fee Portal",
                        subtitle = "Challans & payments",
                        icon = Icons.Default.AccountBalanceWallet,
                        accentColor = Color(0xFFD97706),
                        modifier = Modifier.weight(1f),
                        testTag = "nav_card_fees"
                    ) {
                        viewModel.navigateTo(CrescentScreen.ADMISSIONS_AND_FEES)
                    }

                    FeatureGridCard(
                        title = "Send Message",
                        subtitle = "Direct teacher chat",
                        icon = Icons.Default.Chat,
                        accentColor = CrescentGreenPrimary,
                        modifier = Modifier.weight(1f),
                        testTag = "nav_card_messages"
                    ) {
                        viewModel.navigateTo(CrescentScreen.MESSAGES)
                    }
                }
            }
        }

        // Campus Announcements
        item {
            Text(
                text = "College Announcements & Notice Board",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(announcements) { ann ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (ann.isUrgent) MaterialTheme.colorScheme.errorContainer else CrescentGreenContainer,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = ann.category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (ann.isUrgent) MaterialTheme.colorScheme.onErrorContainer else CrescentGreenPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = ann.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = ann.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = ann.fullContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatColumn(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun FeatureGridCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .height(100.dp)
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

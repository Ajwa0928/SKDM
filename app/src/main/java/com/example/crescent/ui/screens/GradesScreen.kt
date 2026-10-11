package com.example.crescent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.window.Dialog
import com.example.crescent.model.GradeRecord
import com.example.crescent.ui.CrescentViewModel
import com.example.ui.theme.*

@Composable
fun GradesScreen(
    viewModel: CrescentViewModel,
    modifier: Modifier = Modifier
) {
    val student by viewModel.selectedStudent.collectAsState()
    val allGrades by viewModel.grades.collectAsState()

    var selectedTerm by remember { mutableStateOf("Mid-Term Examination 2026") }
    var showReportCardModal by remember { mutableStateOf(false) }

    val studentGrades = remember(allGrades, student?.id, selectedTerm) {
        allGrades.filter { it.studentId == (student?.id ?: "std_101") && it.term == selectedTerm }
    }

    val totalObtained = remember(studentGrades) { studentGrades.sumOf { it.marksObtained } }
    val totalMax = remember(studentGrades) { studentGrades.sumOf { it.totalMarks } }
    val percentage = remember(totalObtained, totalMax) {
        if (totalMax > 0) (totalObtained.toFloat() / totalMax) * 100f else 0f
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // Term Selection Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedTerm == "Mid-Term Examination 2026",
                    onClick = { selectedTerm = "Mid-Term Examination 2026" },
                    label = { Text("Mid-Term 2026", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.WorkspacePremium, null, modifier = Modifier.size(14.dp)) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedTerm == "First Term Assessment",
                    onClick = { selectedTerm = "First Term Assessment" },
                    label = { Text("First Term 2026", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.School, null, modifier = Modifier.size(14.dp)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Summary Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CrescentGreenPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = selectedTerm,
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
                                text = "Grade ${student?.currentGpaOrGrade ?: "A+"}",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2C2200),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ScoreBadge("Total Marks", "$totalObtained / $totalMax")
                        ScoreBadge("Percentage", "${String.format("%.1f", percentage)}%")
                        ScoreBadge("Class Position", student?.rankInClass ?: "2nd")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showReportCardModal = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CrescentGoldSecondary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("view_report_card_button")
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF2C2200))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "View Official Digital Report Card",
                            color = Color(0xFF2C2200),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Subject Results List
        item {
            Text(
                text = "Subject Results Breakdown (${studentGrades.size} Subjects)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(studentGrades) { gradeItem ->
            SubjectGradeCard(gradeItem)
        }

        // Grading Scale Legend
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "BISE Bannu & College Grading Scale",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "A+ (80% & above - Outstanding) • A (70% - 79% - Excellent) • B (60% - 69% - Very Good) • C (50% - 59% - Good) • D (40% - 49% - Pass)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (showReportCardModal) {
        OfficialReportCardDialog(
            student = student,
            term = selectedTerm,
            grades = studentGrades,
            totalObtained = totalObtained,
            totalMax = totalMax,
            percentage = percentage,
            onDismiss = { showReportCardModal = false }
        )
    }
}

@Composable
private fun SubjectGradeCard(grade: GradeRecord) {
    val percentage = (grade.marksObtained.toFloat() / grade.totalMarks) * 100f
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = grade.subject,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${grade.marksObtained}/${grade.totalMarks}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = if (grade.grade == "A+") CrescentGreenContainer else CrescentGoldContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = grade.grade,
                            fontWeight = FontWeight.Bold,
                            color = if (grade.grade == "A+") CrescentGreenPrimary else Color(0xFFB45309),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (percentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (percentage >= 85f) CrescentGreenPrimary else CrescentGoldSecondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Remarks: ${grade.teacherRemarks}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ScoreBadge(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFE2F5ED)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun OfficialReportCardDialog(
    student: com.example.crescent.model.Student?,
    term: String,
    grades: List<GradeRecord>,
    totalObtained: Int,
    totalMax: Int,
    percentage: Float,
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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
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
                        Text(
                            text = "ACADEMIC PROGRESS REPORT • $term",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = CrescentGoldSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 10.dp))

                student?.let { s ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Student: ${s.fullName}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Text("Father: ${s.fatherName} • Roll #: ${s.rollNumber}", style = MaterialTheme.typography.bodySmall)
                            Text("Class: ${s.gradeClass} • Section: ${s.section}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.heightIn(max = 240.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(grades) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.subject, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                            Text("${item.marksObtained}/${item.totalMarks}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(item.grade, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = CrescentGreenPrimary)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total: $totalObtained / $totalMax", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Text("Percentage: ${String.format("%.1f", percentage)}%", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = CrescentGreenPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Class Incharge", style = MaterialTheme.typography.labelSmall)
                        Text("Prof. Abdul Qayyum", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Principal Signature", style = MaterialTheme.typography.labelSmall)
                        Text("Certified Official", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = CrescentGreenPrimary)
                    }
                }
            }
        }
    }
}

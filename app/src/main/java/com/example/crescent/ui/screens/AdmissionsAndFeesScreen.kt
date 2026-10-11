package com.example.crescent.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.crescent.model.AdmissionApplication
import com.example.crescent.model.ApplicationStatus
import com.example.crescent.model.FeeVoucher
import com.example.crescent.ui.CrescentViewModel
import com.example.crescent.ui.components.FeeChallanDialog
import com.example.ui.theme.*

@Composable
fun AdmissionsAndFeesScreen(
    viewModel: CrescentViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Admission Form, 1: Fee Portal
    val activeVoucherForChallan by viewModel.selectedVoucherForChallan.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = CrescentGreenPrimary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Online Admission Form", fontWeight = FontWeight.SemiBold, fontSize = 13.sp) },
                icon = { Icon(Icons.Default.AppRegistration, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_admission_form")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Fee Portal & Vouchers", fontWeight = FontWeight.SemiBold, fontSize = 13.sp) },
                icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_fee_portal")
            )
        }

        if (selectedTab == 0) {
            AdmissionFormTab(viewModel)
        } else {
            FeePortalTab(viewModel)
        }
    }

    activeVoucherForChallan?.let { voucher ->
        FeeChallanDialog(
            voucher = voucher,
            onPay = {
                viewModel.payFeeVoucher(voucher.voucherId)
                viewModel.setSelectedVoucher(null)
            },
            onDismiss = { viewModel.setSelectedVoucher(null) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdmissionFormTab(viewModel: CrescentViewModel) {
    val admissions by viewModel.admissions.collectAsState()

    var applicantName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var cnicOrBForm by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("15 March 2009") }
    var gender by remember { mutableStateOf("Male") }
    var phone by remember { mutableStateOf("+92 30") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("Mirali, North Waziristan Tribal District") }
    var selectedProgram by remember { mutableStateOf("F.Sc Pre-Medical") }
    var prevSchool by remember { mutableStateOf("Govt High School Mirali") }
    var prevMarks by remember { mutableStateOf("950") }
    var prevTotal by remember { mutableStateOf("1100") }

    var submittedAppDialog by remember { mutableStateOf<AdmissionApplication?>(null) }
    var formSubmittedSuccess by remember { mutableStateOf(false) }

    val programs = listOf(
        "F.Sc Pre-Medical",
        "F.Sc Pre-Engineering",
        "ICS (Computer Science)",
        "F.A Humanities",
        "Matriculation (Science)",
        "Associate Degree (ADP)"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // College Admission Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CrescentGreenPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        color = CrescentGoldSecondary,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "ADMISSIONS OPEN • SESSION 2026-27",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C2200),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Student Admission Application",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "The Crescent School and Degree College Mirali NWTD offers modern laboratories, BISE Bannu affiliated curriculum, and merit scholarships.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE2F5ED)
                    )
                }
            }
        }

        // Form Fields Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Applicant Information",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = CrescentGreenPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = applicantName,
                        onValueChange = { applicantName = it },
                        label = { Text("Applicant Full Name *") },
                        placeholder = { Text("e.g. Asad Ullah Dawar") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_applicant_name")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = fatherName,
                        onValueChange = { fatherName = it },
                        label = { Text("Father's / Guardian Name *") },
                        placeholder = { Text("e.g. Malik Mir Kalam") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_father_name")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = cnicOrBForm,
                            onValueChange = { cnicOrBForm = it },
                            label = { Text("B-Form / CNIC *") },
                            placeholder = { Text("12101-XXXXXXX-X") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_bform")
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number *") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_phone")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Residential Address (Mirali / NWTD) *") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "2. Program Applied For",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = CrescentGreenPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Program Selector Chips
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        programs.chunked(2).forEach { rowPrograms ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowPrograms.forEach { prog ->
                                    FilterChip(
                                        selected = selectedProgram == prog,
                                        onClick = { selectedProgram = prog },
                                        label = { Text(prog, fontSize = 11.sp) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "3. Academic Background (Previous School)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = CrescentGreenPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = prevSchool,
                        onValueChange = { prevSchool = it },
                        label = { Text("Previous School / Board Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = prevMarks,
                            onValueChange = { prevMarks = it },
                            label = { Text("Marks Obtained") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = prevTotal,
                            onValueChange = { prevTotal = it },
                            label = { Text("Total Marks") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val name = applicantName.ifBlank { "Asad Ullah Dawar" }
                            val fName = fatherName.ifBlank { "Malik Mir Kalam" }
                            val cnic = cnicOrBForm.ifBlank { "12101-8934120-1" }
                            val pMarks = prevMarks.toIntOrNull() ?: 950
                            val pTotal = prevTotal.toIntOrNull() ?: 1100

                            val app = viewModel.submitAdmission(
                                applicantName = name,
                                fatherName = fName,
                                cnicOrBForm = cnic,
                                dob = dob,
                                gender = gender,
                                phone = phone,
                                email = email.ifBlank { "applicant@mirali.edu.pk" },
                                address = address,
                                program = selectedProgram,
                                prevSchool = prevSchool,
                                prevMarks = pMarks,
                                prevTotal = pTotal
                            )
                            submittedAppDialog = app
                            formSubmittedSuccess = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CrescentGreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_admission_form_button")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit Online Admission Application")
                    }
                }
            }
        }

        // Track Submitted Applications
        item {
            Text(
                text = "Submitted Applications Tracker (${admissions.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(admissions) { app ->
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
                            text = app.id,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CrescentGreenPrimary
                        )

                        Surface(
                            color = when (app.status) {
                                ApplicationStatus.SUBMITTED -> CrescentGoldContainer
                                ApplicationStatus.UNDER_REVIEW -> Color(0xFFDBEAFE)
                                ApplicationStatus.VERIFIED, ApplicationStatus.ADMITTED -> Color(0xFFDCFCE7)
                            },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = app.status.name.replace("_", " "),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (app.status) {
                                    ApplicationStatus.SUBMITTED -> Color(0xFFB45309)
                                    ApplicationStatus.UNDER_REVIEW -> Color(0xFF1D4ED8)
                                    ApplicationStatus.VERIFIED, ApplicationStatus.ADMITTED -> StatusPresent
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${app.applicantName} s/d/o ${app.fatherName}",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Applied For: ${app.programApplied} • Date: ${app.submissionDate}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Score: ${app.previousMarks}/${app.previousTotalMarks} • ${app.previousInstitution}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    submittedAppDialog?.let { app ->
        AlertDialog(
            onDismissRequest = { submittedAppDialog = null },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusPresent, modifier = Modifier.size(36.dp))
            },
            title = {
                Text("Admission Application Submitted!", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            },
            text = {
                Column {
                    Text("Your application has been registered successfully at The Crescent School & Degree College Mirali.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = CrescentGreenContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Application ID: ${app.id}", fontWeight = FontWeight.Bold, color = CrescentGreenPrimary)
                            Text("Applicant: ${app.applicantName}")
                            Text("Program: ${app.programApplied}")
                            Text("Status: Under Office Review")
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Please present this ID along with verified documents at Mirali Campus Admission Desk.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { submittedAppDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = CrescentGreenPrimary)
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
private fun FeePortalTab(viewModel: CrescentViewModel) {
    val student by viewModel.selectedStudent.collectAsState()
    val vouchers by viewModel.feeVouchers.collectAsState()

    val studentVouchers = remember(vouchers, student?.id) {
        vouchers.filter { it.studentId == (student?.id ?: "std_101") }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // Fee Balance Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if ((student?.feeDueAmount ?: 0) > 0) Color(0xFFFFFBEB) else Color(0xFFF0FDF4)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if ((student?.feeDueAmount ?: 0) > 0) Color(0xFFD97706) else StatusPresent
                    )
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Institutional Dues Status",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${student?.fullName ?: "Student"} • ${student?.gradeClass}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            color = if ((student?.feeDueAmount ?: 0) > 0) Color(0xFFD97706) else StatusPresent,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if ((student?.feeDueAmount ?: 0) > 0) "PAYMENT DUE" else "ALL CLEARED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "Current Payable Balance:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if ((student?.feeDueAmount ?: 0) > 0) "PKR ${student?.feeDueAmount}" else "PKR 0",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if ((student?.feeDueAmount ?: 0) > 0) Color(0xFFB45309) else StatusPresent
                            )
                        }

                        if ((student?.feeDueAmount ?: 0) > 0) {
                            Text(
                                text = "Due: ${student?.feeDueDate}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Automated notification trigger demo button
                    FilledTonalButton(
                        onClick = { viewModel.triggerFeeReminderDemo() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Trigger Automated Fee Reminder Alert")
                    }
                }
            }
        }

        // Vouchers List
        item {
            Text(
                text = "Fee Challans & Payment History",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }

        items(studentVouchers) { voucher ->
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
                        Column {
                            Text(
                                text = voucher.monthYear,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Challan: ${voucher.voucherId}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            color = if (voucher.isPaid) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (voucher.isPaid) "PAID" else "UNPAID",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (voucher.isPaid) StatusPresent else Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Payable: PKR ${voucher.totalAmount}", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = if (voucher.isPaid) "Paid on ${voucher.paidDate}" else "Due by ${voucher.dueDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (voucher.isPaid) StatusPresent else MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.setSelectedVoucher(voucher) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("View Challan", fontSize = 12.sp)
                        }

                        if (!voucher.isPaid) {
                            Button(
                                onClick = { viewModel.payFeeVoucher(voucher.voucherId) },
                                colors = ButtonDefaults.buttonColors(containerColor = CrescentGreenPrimary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Pay Online", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

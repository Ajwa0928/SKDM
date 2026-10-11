package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crescent.ui.CrescentScreen
import com.example.crescent.ui.CrescentViewModel
import com.example.crescent.ui.components.CrescentTopAppBar
import com.example.crescent.ui.components.NotificationsDialog
import com.example.crescent.ui.components.StudentSwitcherDialog
import com.example.crescent.ui.screens.*
import com.example.ui.theme.CrescentTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CrescentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrescentTheme {
                MainAppScaffold(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScaffold(viewModel: CrescentViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val showNotifications by viewModel.showNotificationsDialog.collectAsState()
    val showStudentSwitcher by viewModel.showStudentSwitcher.collectAsState()

    // Handle back press to return to Dashboard
    if (currentScreen != CrescentScreen.DASHBOARD) {
        BackHandler {
            viewModel.navigateTo(CrescentScreen.DASHBOARD)
        }
    }

    Scaffold(
        topBar = {
            CrescentTopAppBar(viewModel = viewModel)
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentScreen == CrescentScreen.DASHBOARD,
                    onClick = { viewModel.navigateTo(CrescentScreen.DASHBOARD) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == CrescentScreen.DASHBOARD) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                            contentDescription = "Dashboard"
                        )
                    },
                    label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("bottom_nav_dashboard")
                )

                NavigationBarItem(
                    selected = currentScreen == CrescentScreen.ATTENDANCE,
                    onClick = { viewModel.navigateTo(CrescentScreen.ATTENDANCE) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == CrescentScreen.ATTENDANCE) Icons.Filled.FactCheck else Icons.Outlined.FactCheck,
                            contentDescription = "Attendance"
                        )
                    },
                    label = { Text("Attendance", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("bottom_nav_attendance")
                )

                NavigationBarItem(
                    selected = currentScreen == CrescentScreen.GRADES,
                    onClick = { viewModel.navigateTo(CrescentScreen.GRADES) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == CrescentScreen.GRADES) Icons.Filled.Grade else Icons.Outlined.Grade,
                            contentDescription = "Grades"
                        )
                    },
                    label = { Text("Grades", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("bottom_nav_grades")
                )

                NavigationBarItem(
                    selected = currentScreen == CrescentScreen.TEACHERS,
                    onClick = { viewModel.navigateTo(CrescentScreen.TEACHERS) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == CrescentScreen.TEACHERS) Icons.Filled.School else Icons.Outlined.School,
                            contentDescription = "Teachers"
                        )
                    },
                    label = { Text("Teachers", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("bottom_nav_teachers")
                )

                NavigationBarItem(
                    selected = currentScreen == CrescentScreen.ADMISSIONS_AND_FEES,
                    onClick = { viewModel.navigateTo(CrescentScreen.ADMISSIONS_AND_FEES) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == CrescentScreen.ADMISSIONS_AND_FEES) Icons.Filled.AppRegistration else Icons.Outlined.AppRegistration,
                            contentDescription = "Admissions & Fees"
                        )
                    },
                    label = { Text("Admission", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("bottom_nav_admissions")
                )

                NavigationBarItem(
                    selected = currentScreen == CrescentScreen.MESSAGES,
                    onClick = { viewModel.navigateTo(CrescentScreen.MESSAGES) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == CrescentScreen.MESSAGES) Icons.Filled.Chat else Icons.Outlined.Chat,
                            contentDescription = "Messages"
                        )
                    },
                    label = { Text("Messages", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("bottom_nav_messages")
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                CrescentScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                CrescentScreen.ATTENDANCE -> AttendanceScreen(viewModel = viewModel)
                CrescentScreen.GRADES -> GradesScreen(viewModel = viewModel)
                CrescentScreen.TEACHERS -> TeachersScreen(viewModel = viewModel)
                CrescentScreen.ADMISSIONS_AND_FEES -> AdmissionsAndFeesScreen(viewModel = viewModel)
                CrescentScreen.MESSAGES -> MessagesScreen(viewModel = viewModel)
            }
        }
    }

    if (showNotifications) {
        NotificationsDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setShowNotifications(false) }
        )
    }

    if (showStudentSwitcher) {
        StudentSwitcherDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setShowStudentSwitcher(false) }
        )
    }
}

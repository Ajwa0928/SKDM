package com.example.crescent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crescent.data.CrescentRepository
import com.example.crescent.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class CrescentScreen(val title: String) {
    DASHBOARD("Dashboard"),
    ATTENDANCE("Attendance"),
    GRADES("Academic Grades"),
    TEACHERS("Faculty Profiles"),
    ADMISSIONS_AND_FEES("Admissions & Fees"),
    MESSAGES("Messages")
}

class CrescentViewModel(
    val repository: CrescentRepository = CrescentRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(CrescentScreen.DASHBOARD)
    val currentScreen: StateFlow<CrescentScreen> = _currentScreen.asStateFlow()

    private val _showNotificationsDialog = MutableStateFlow(false)
    val showNotificationsDialog: StateFlow<Boolean> = _showNotificationsDialog.asStateFlow()

    private val _showStudentSwitcher = MutableStateFlow(false)
    val showStudentSwitcher: StateFlow<Boolean> = _showStudentSwitcher.asStateFlow()

    // Notification filters
    private val _selectedNotificationFilter = MutableStateFlow<NotificationCategory?>(null)
    val selectedNotificationFilter: StateFlow<NotificationCategory?> = _selectedNotificationFilter.asStateFlow()

    // Teacher filters
    private val _selectedTeacherDepartment = MutableStateFlow("All")
    val selectedTeacherDepartment: StateFlow<String> = _selectedTeacherDepartment.asStateFlow()

    // Active teacher detail dialog
    private val _selectedTeacherForDetail = MutableStateFlow<Teacher?>(null)
    val selectedTeacherForDetail: StateFlow<Teacher?> = _selectedTeacherForDetail.asStateFlow()

    // Active Fee voucher dialog
    private val _selectedVoucherForChallan = MutableStateFlow<FeeVoucher?>(null)
    val selectedVoucherForChallan: StateFlow<FeeVoucher?> = _selectedVoucherForChallan.asStateFlow()

    // Direct message compose recipient pre-select
    private val _composeRecipient = MutableStateFlow<Pair<String, String>?>(null)
    val composeRecipient: StateFlow<Pair<String, String>?> = _composeRecipient.asStateFlow()

    // Attendance filter
    private val _attendanceFilter = MutableStateFlow<AttendanceStatus?>(null)
    val attendanceFilter: StateFlow<AttendanceStatus?> = _attendanceFilter.asStateFlow()

    // Repository flows
    val students: StateFlow<List<Student>> = repository.students
    val selectedStudent: StateFlow<Student?> = repository.selectedStudent
    val attendanceRecords: StateFlow<List<AttendanceRecord>> = repository.attendanceRecords
    val grades: StateFlow<List<GradeRecord>> = repository.grades
    val teachers: StateFlow<List<Teacher>> = repository.teachers
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val admissions: StateFlow<List<AdmissionApplication>> = repository.admissions
    val messages: StateFlow<List<MessageItem>> = repository.messages
    val feeVouchers: StateFlow<List<FeeVoucher>> = repository.feeVouchers
    val announcements: StateFlow<List<Announcement>> = repository.announcements

    val unreadNotificationsCount: StateFlow<Int> = notifications.combine(_selectedNotificationFilter) { notifs, _ ->
        notifs.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 3)

    fun navigateTo(screen: CrescentScreen) {
        _currentScreen.value = screen
    }

    fun setShowNotifications(show: Boolean) {
        _showNotificationsDialog.value = show
    }

    fun setShowStudentSwitcher(show: Boolean) {
        _showStudentSwitcher.value = show
    }

    fun selectStudent(student: Student) {
        repository.selectStudent(student)
        _showStudentSwitcher.value = false
    }

    fun setNotificationFilter(category: NotificationCategory?) {
        _selectedNotificationFilter.value = category
    }

    fun setTeacherDepartment(dept: String) {
        _selectedTeacherDepartment.value = dept
    }

    fun setSelectedTeacher(teacher: Teacher?) {
        _selectedTeacherForDetail.value = teacher
    }

    fun setSelectedVoucher(voucher: FeeVoucher?) {
        _selectedVoucherForChallan.value = voucher
    }

    fun setComposeRecipient(name: String, role: String) {
        _composeRecipient.value = Pair(name, role)
        _currentScreen.value = CrescentScreen.MESSAGES
    }

    fun clearComposeRecipient() {
        _composeRecipient.value = null
    }

    fun setAttendanceFilter(status: AttendanceStatus?) {
        _attendanceFilter.value = status
    }

    fun markAttendance(status: AttendanceStatus, subject: String, remarks: String) {
        val studentId = selectedStudent.value?.id ?: "std_101"
        repository.markAttendance(studentId, status, subject, remarks)
    }

    fun submitAdmission(
        applicantName: String,
        fatherName: String,
        cnicOrBForm: String,
        dob: String,
        gender: String,
        phone: String,
        email: String,
        address: String,
        program: String,
        prevSchool: String,
        prevMarks: Int,
        prevTotal: Int
    ): AdmissionApplication {
        return repository.submitAdmission(
            applicantName,
            fatherName,
            cnicOrBForm,
            dob,
            gender,
            phone,
            email,
            address,
            program,
            prevSchool,
            prevMarks,
            prevTotal
        )
    }

    fun sendMessage(recipientName: String, recipientRole: String, subject: String, text: String): MessageItem {
        return repository.sendMessage(recipientName, recipientRole, subject, text)
    }

    fun payFeeVoucher(voucherId: String) {
        repository.payFeeVoucher(voucherId)
    }

    fun markAllNotificationsRead() {
        repository.markAllNotificationsRead()
    }

    fun markNotificationRead(id: String) {
        repository.markNotificationRead(id)
    }

    fun triggerExamNotificationDemo() {
        repository.addNotification(
            title = "Automated Exam Alert: Pre-Board Date Sheet",
            message = "Pre-Board Intermediate Part II BISE examination timetable has been finalized. Papers begin Oct 20 at Crescent Mirali Hall.",
            category = NotificationCategory.EXAM,
            dueDateOrDate = "Starts 20 Oct 2026",
            priorityBadge = "EXAM DATE SHEET"
        )
    }

    fun triggerFeeReminderDemo() {
        repository.addNotification(
            title = "Automated Fee Alert: 3 Days Remaining",
            message = "Notice for Haji Gul Rehman: Monthly institutional dues of PKR 4,500 due on 15 Oct. Online voucher payment enabled.",
            category = NotificationCategory.FEE,
            dueDateOrDate = "Due: 15 Oct 2026",
            priorityBadge = "DUE SOON"
        )
    }
}

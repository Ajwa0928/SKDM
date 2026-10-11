package com.example.crescent.model

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    LATE,
    LEAVE
}

enum class NotificationCategory {
    EXAM,
    FEE,
    ATTENDANCE,
    GENERAL
}

enum class ApplicationStatus {
    SUBMITTED,
    UNDER_REVIEW,
    VERIFIED,
    ADMITTED
}

data class Student(
    val id: String,
    val fullName: String,
    val rollNumber: String,
    val gradeClass: String,
    val section: String,
    val admissionNo: String,
    val fatherName: String,
    val contactNumber: String,
    val attendancePercentage: Float,
    val currentGpaOrGrade: String,
    val rankInClass: String,
    val feeDueAmount: Int,
    val feeDueDate: String
)

data class AttendanceRecord(
    val id: String,
    val studentId: String,
    val date: String,
    val status: AttendanceStatus,
    val subject: String,
    val teacherName: String,
    val remarks: String
)

data class GradeRecord(
    val id: String,
    val studentId: String,
    val term: String,
    val subject: String,
    val marksObtained: Int,
    val totalMarks: Int,
    val grade: String,
    val teacherRemarks: String
)

data class Teacher(
    val id: String,
    val name: String,
    val designation: String,
    val department: String,
    val qualification: String,
    val experienceYears: Int,
    val subjectsTaught: List<String>,
    val email: String,
    val phone: String,
    val officeHours: String,
    val bio: String,
    val isClassTeacherOf: String? = null
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val category: NotificationCategory,
    val timestamp: String,
    val isRead: Boolean = false,
    val dueDateOrDate: String? = null,
    val priorityBadge: String? = null
)

data class AdmissionApplication(
    val id: String,
    val applicantName: String,
    val fatherName: String,
    val cnicOrBForm: String,
    val dateOfBirth: String,
    val gender: String,
    val contactNumber: String,
    val email: String,
    val address: String,
    val programApplied: String,
    val previousInstitution: String,
    val previousMarks: Int,
    val previousTotalMarks: Int,
    val status: ApplicationStatus,
    val submissionDate: String
)

data class MessageItem(
    val id: String,
    val threadId: String,
    val senderName: String,
    val senderRole: String,
    val recipientName: String,
    val recipientRole: String,
    val subject: String,
    val messageText: String,
    val timestamp: String,
    val isFromUser: Boolean,
    val isRead: Boolean = true
)

data class FeeVoucher(
    val voucherId: String,
    val studentId: String,
    val studentName: String,
    val rollNumber: String,
    val monthYear: String,
    val tuitionFee: Int,
    val examinationFee: Int,
    val labCharges: Int,
    val libraryCharges: Int,
    val totalAmount: Int,
    val issueDate: String,
    val dueDate: String,
    val isPaid: Boolean,
    val paidDate: String? = null,
    val bankBranch: String = "National Bank of Pakistan, Mirali Branch"
)

data class Announcement(
    val id: String,
    val title: String,
    val date: String,
    val category: String,
    val summary: String,
    val fullContent: String,
    val isUrgent: Boolean = false
)

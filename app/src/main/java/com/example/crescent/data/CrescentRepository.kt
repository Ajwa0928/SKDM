package com.example.crescent.data

import com.example.crescent.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CrescentRepository {

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _selectedStudent = MutableStateFlow<Student?>(null)
    val selectedStudent: StateFlow<Student?> = _selectedStudent.asStateFlow()

    private val _attendanceRecords = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val attendanceRecords: StateFlow<List<AttendanceRecord>> = _attendanceRecords.asStateFlow()

    private val _grades = MutableStateFlow<List<GradeRecord>>(emptyList())
    val grades: StateFlow<List<GradeRecord>> = _grades.asStateFlow()

    private val _teachers = MutableStateFlow<List<Teacher>>(emptyList())
    val teachers: StateFlow<List<Teacher>> = _teachers.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _admissions = MutableStateFlow<List<AdmissionApplication>>(emptyList())
    val admissions: StateFlow<List<AdmissionApplication>> = _admissions.asStateFlow()

    private val _messages = MutableStateFlow<List<MessageItem>>(emptyList())
    val messages: StateFlow<List<MessageItem>> = _messages.asStateFlow()

    private val _feeVouchers = MutableStateFlow<List<FeeVoucher>>(emptyList())
    val feeVouchers: StateFlow<List<FeeVoucher>> = _feeVouchers.asStateFlow()

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        val s1 = Student(
            id = "std_101",
            fullName = "Muhammad Bilal Dawar",
            rollNumber = "24-MED-042",
            gradeClass = "F.Sc Pre-Medical (2nd Year)",
            section = "Section A (Jinnah Hall)",
            admissionNo = "CSDC-2024-042",
            fatherName = "Haji Gul Rehman Dawar",
            contactNumber = "+92 301 8974521",
            attendancePercentage = 94.2f,
            currentGpaOrGrade = "A+",
            rankInClass = "2nd of 65",
            feeDueAmount = 4500,
            feeDueDate = "15 Oct 2026"
        )
        val s2 = Student(
            id = "std_102",
            fullName = "Ayesha Rehman Wazir",
            rollNumber = "25-ICS-018",
            gradeClass = "ICS Computer Science (1st Year)",
            section = "Section B (Iqbal Wing)",
            admissionNo = "CSDC-2025-018",
            fatherName = "Malik Abdul Rehman Wazir",
            contactNumber = "+92 306 9123847",
            attendancePercentage = 96.8f,
            currentGpaOrGrade = "A+",
            rankInClass = "1st of 58",
            feeDueAmount = 0,
            feeDueDate = "Paid"
        )
        val s3 = Student(
            id = "std_103",
            fullName = "Hamza Noor Dawar",
            rollNumber = "26-MAT-105",
            gradeClass = "Matriculation (10th Grade)",
            section = "Section Science-A",
            admissionNo = "CSDC-2023-105",
            fatherName = "Noor Muhammad Dawar",
            contactNumber = "+92 303 5541289",
            attendancePercentage = 89.5f,
            currentGpaOrGrade = "A",
            rankInClass = "5th of 72",
            feeDueAmount = 3800,
            feeDueDate = "18 Oct 2026"
        )
        _students.value = listOf(s1, s2, s3)
        _selectedStudent.value = s1

        // Teachers
        val t1 = Teacher(
            id = "t_1",
            name = "Prof. Dr. Abdul Qayyum Dawar",
            designation = "Principal & Professor of Chemistry",
            department = "Science & Pre-Medical",
            qualification = "Ph.D. Chemistry, Quaid-i-Azam University Islamabad",
            experienceYears = 22,
            subjectsTaught = listOf("Organic Chemistry", "Analytical Chemistry", "Pre-Medical Seminar"),
            email = "principal@crescentschoolmirali.edu.pk",
            phone = "+92 928 221045",
            officeHours = "Mon - Thu: 10:00 AM - 12:30 PM",
            bio = "Serving Crescent School & Degree College Mirali for over two decades. Mentored hundreds of students into King Edward, Khyber Medical College, and UET.",
            isClassTeacherOf = "F.Sc Pre-Medical (2nd Year)"
        )
        val t2 = Teacher(
            id = "t_2",
            name = "Assoc. Prof. Tariq Mehmood Wazir",
            designation = "Head of Physics Department",
            department = "Physics & Applied Sciences",
            qualification = "M.Phil Physics, University of Peshawar",
            experienceYears = 16,
            subjectsTaught = listOf("Physics Part-I", "Physics Part-II", "Modern Physics"),
            email = "tariq.physics@crescentschoolmirali.edu.pk",
            phone = "+92 300 4872110",
            officeHours = "Tue & Thu: 11:30 AM - 01:30 PM",
            bio = "Specialist in Nuclear and Wave Physics with practical laboratory coaching for BISE Bannu Board distinctions.",
            isClassTeacherOf = "F.Sc Pre-Engineering (1st Year)"
        )
        val t3 = Teacher(
            id = "t_3",
            name = "Madam Zulaikha Bibi",
            designation = "Senior Lecturer in Biology",
            department = "Biological Sciences",
            qualification = "M.Phil Zoology, Kohat University of Science & Tech",
            experienceYears = 10,
            subjectsTaught = listOf("Botany", "Zoology", "Cell Biology & Genetics"),
            email = "zulaikha.bio@crescentschoolmirali.edu.pk",
            phone = "+92 334 6610992",
            officeHours = "Mon & Wed: 09:00 AM - 11:00 AM",
            bio = "Dedicated to interactive biological models and MDCAT preparation for female & male medical aspirants in NWTD.",
            isClassTeacherOf = null
        )
        val t4 = Teacher(
            id = "t_4",
            name = "Engr. Farhan Ullah",
            designation = "Lecturer in Computer Science & AI",
            department = "Computer Science & IT",
            qualification = "MS Software Engineering, NUST Islamabad",
            experienceYears = 7,
            subjectsTaught = listOf("C++ & Python", "Database Systems", "ICS Computer Science"),
            email = "farhan.cs@crescentschoolmirali.edu.pk",
            phone = "+92 345 7712390",
            officeHours = "Mon - Fri: 01:00 PM - 02:30 PM",
            bio = "Lead coordinator of Mirali STEM Club and computer lab digitalization. Passionate about empowering local youth in programming.",
            isClassTeacherOf = "ICS Computer Science (1st Year)"
        )
        val t5 = Teacher(
            id = "t_5",
            name = "Sir Noor Muhammad",
            designation = "Head of Mathematics Department",
            department = "Mathematics",
            qualification = "M.Sc Pure Mathematics, University of Malakand",
            experienceYears = 15,
            subjectsTaught = listOf("Calculus", "Trigonometry", "Algebra & Vectors"),
            email = "noor.math@crescentschoolmirali.edu.pk",
            phone = "+92 301 9845123",
            officeHours = "Wed & Fri: 10:00 AM - 12:00 PM",
            bio = "Renowned for simplifying complex calculus and vector problems for intermediate board toppers.",
            isClassTeacherOf = null
        )
        val t6 = Teacher(
            id = "t_6",
            name = "Prof. Aslam Pervez",
            designation = "Senior Lecturer English & Literature",
            department = "Humanities & Languages",
            qualification = "M.A English Literature, National University of Modern Languages",
            experienceYears = 13,
            subjectsTaught = listOf("English Compulsory", "Essay Writing & Grammar"),
            email = "aslam.eng@crescentschoolmirali.edu.pk",
            phone = "+92 308 1122334",
            officeHours = "Mon - Wed: 11:00 AM - 01:00 PM",
            bio = "Debating club mentor, helping students develop eloquent Pashto, Urdu, and English communication skills.",
            isClassTeacherOf = null
        )
        _teachers.value = listOf(t1, t2, t3, t4, t5, t6)

        // Attendance records for Muhammad Bilal
        _attendanceRecords.value = listOf(
            AttendanceRecord("att_1", "std_101", "10 Oct 2026", AttendanceStatus.PRESENT, "Organic Chemistry", "Prof. Dr. Abdul Qayyum", "On time, participated actively in titration practical"),
            AttendanceRecord("att_2", "std_101", "10 Oct 2026", AttendanceStatus.PRESENT, "Physics Lab", "Assoc. Prof. Tariq Mehmood", "Completed spectrometer experiment"),
            AttendanceRecord("att_3", "std_101", "09 Oct 2026", AttendanceStatus.PRESENT, "Botany & Zoology", "Madam Zulaikha Bibi", "Cell division revision notes submitted"),
            AttendanceRecord("att_4", "std_101", "09 Oct 2026", AttendanceStatus.PRESENT, "English Compulsory", "Prof. Aslam Pervez", "Essay presentation appreciated"),
            AttendanceRecord("att_5", "std_101", "08 Oct 2026", AttendanceStatus.LATE, "Islamic Studies", "Qari Rehmat Ullah", "Late by 10 mins due to local transport delay"),
            AttendanceRecord("att_6", "std_101", "07 Oct 2026", AttendanceStatus.PRESENT, "Chemistry Theory", "Prof. Dr. Abdul Qayyum", "Present"),
            AttendanceRecord("att_7", "std_101", "06 Oct 2026", AttendanceStatus.PRESENT, "Physics Theory", "Assoc. Prof. Tariq Mehmood", "Present"),
            AttendanceRecord("att_8", "std_101", "05 Oct 2026", AttendanceStatus.PRESENT, "Biology Seminar", "Madam Zulaikha Bibi", "Present"),
            AttendanceRecord("att_9", "std_101", "03 Oct 2026", AttendanceStatus.LEAVE, "Full Day", "College Administration", "Authorized medical leave approved by Principal"),
            AttendanceRecord("att_10", "std_101", "02 Oct 2026", AttendanceStatus.PRESENT, "All Subjects", "Faculty", "Full day attendance"),
            AttendanceRecord("att_11", "std_101", "01 Oct 2026", AttendanceStatus.PRESENT, "All Subjects", "Faculty", "Full day attendance"),
            AttendanceRecord("att_12", "std_101", "30 Sep 2026", AttendanceStatus.ABSENT, "Physics Lab", "Assoc. Prof. Tariq Mehmood", "Unannounced absence recorded"),
            AttendanceRecord("att_13", "std_101", "29 Sep 2026", AttendanceStatus.PRESENT, "Chemistry Theory", "Prof. Dr. Abdul Qayyum", "Present")
        )

        // Grades for Bilal (std_101)
        _grades.value = listOf(
            GradeRecord("grd_1", "std_101", "Mid-Term Examination 2026", "Chemistry", 94, 100, "A+", "Excellent mastery of organic reaction mechanisms."),
            GradeRecord("grd_2", "std_101", "Mid-Term Examination 2026", "Physics", 88, 100, "A+", "Strong analytical grasp of electromagnetism and circuits."),
            GradeRecord("grd_3", "std_101", "Mid-Term Examination 2026", "Biology", 92, 100, "A+", "Outstanding diagrammatic clarity in human physiology."),
            GradeRecord("grd_4", "std_101", "Mid-Term Examination 2026", "English Compulsory", 84, 100, "A", "Good vocabulary, minor grammatical syntax focus needed."),
            GradeRecord("grd_5", "std_101", "Mid-Term Examination 2026", "Urdu Literature", 82, 100, "A", "High marks in Tashreeh and Ghazal analysis."),
            GradeRecord("grd_6", "std_101", "Mid-Term Examination 2026", "Islamic Studies", 48, 50, "A+", "Exceptional memorization and Quranic references."),
            GradeRecord("grd_7", "std_101", "Mid-Term Examination 2026", "Pakistan Studies", 46, 50, "A+", "Thorough understanding of regional constitutional history."),

            // First Term
            GradeRecord("grd_8", "std_101", "First Term Assessment", "Chemistry", 89, 100, "A+", "Very good laboratory aptitude."),
            GradeRecord("grd_9", "std_101", "First Term Assessment", "Physics", 85, 100, "A", "Consistent performance."),
            GradeRecord("grd_10", "std_101", "First Term Assessment", "Biology", 88, 100, "A+", "High test scores in cytology."),
            GradeRecord("grd_11", "std_101", "First Term Assessment", "English Compulsory", 79, 100, "A", "Satisfactory comprehension."),
            GradeRecord("grd_12", "std_101", "First Term Assessment", "Urdu Literature", 78, 100, "A", "Good written skills.")
        )

        // Automated Notifications (Exams & Fee reminders)
        _notifications.value = listOf(
            NotificationItem(
                id = "notif_1",
                title = "Fee Payment Reminder - October 2026",
                message = "The college monthly tuition fee of PKR 4,500 for October 2026 is due by 15 Oct 2026. Please deposit at National Bank of Pakistan Mirali Branch or pay via the Fee Portal to avoid late surcharge of PKR 200.",
                category = NotificationCategory.FEE,
                timestamp = "Today, 08:30 AM",
                isRead = false,
                dueDateOrDate = "Due: 15 Oct 2026",
                priorityBadge = "FEE REMINDER"
            ),
            NotificationItem(
                id = "notif_2",
                title = "Upcoming Exam: BISE Chemistry Pre-Board",
                message = "The Chemistry Pre-Board Model Exam (Theory & Practical) is scheduled for Monday, 20 Oct 2026 at 09:00 AM in Examination Hall 1. Syllabus covers Chapters 1-8. Bring your official Crescent College roll slip.",
                category = NotificationCategory.EXAM,
                timestamp = "Yesterday, 04:15 PM",
                isRead = false,
                dueDateOrDate = "Date: 20 Oct 2026 (09:00 AM)",
                priorityBadge = "UPCOMING EXAM"
            ),
            NotificationItem(
                id = "notif_3",
                title = "Upcoming Exam: Physics Practical Viva",
                message = "Physics practical viva-voce and notebook evaluations for 2nd Year Pre-Medical will be conducted on Thursday, 23 Oct 2026 under Assoc. Prof. Tariq Mehmood.",
                category = NotificationCategory.EXAM,
                timestamp = "09 Oct 2026",
                isRead = false,
                dueDateOrDate = "Date: 23 Oct 2026",
                priorityBadge = "PRACTICAL EXAM"
            ),
            NotificationItem(
                id = "notif_4",
                title = "Fee Clearance for Pre-Board Examination",
                message = "All enrolled intermediate students are advised that examination admit cards will be issued from the Accounts Office upon clearing pending dues by Oct 18.",
                category = NotificationCategory.FEE,
                timestamp = "08 Oct 2026",
                isRead = true,
                dueDateOrDate = "Clearance: 18 Oct 2026",
                priorityBadge = "FEE NOTICE"
            ),
            NotificationItem(
                id = "notif_5",
                title = "Merit Scholarship Announced for NWTD Students",
                message = "The Crescent College Governing Board announces 50% tuition merit scholarships for top 5 position holders in the recent Mid-Term evaluations.",
                category = NotificationCategory.GENERAL,
                timestamp = "05 Oct 2026",
                isRead = true,
                dueDateOrDate = null,
                priorityBadge = "ANNOUNCEMENT"
            )
        )

        // Fee Vouchers
        _feeVouchers.value = listOf(
            FeeVoucher(
                voucherId = "VCH-2026-10-042",
                studentId = "std_101",
                studentName = "Muhammad Bilal Dawar",
                rollNumber = "24-MED-042",
                monthYear = "October 2026",
                tuitionFee = 3500,
                examinationFee = 500,
                labCharges = 300,
                libraryCharges = 200,
                totalAmount = 4500,
                issueDate = "01 Oct 2026",
                dueDate = "15 Oct 2026",
                isPaid = false,
                bankBranch = "National Bank of Pakistan, Mirali Branch (A/C: 0142-983011)"
            ),
            FeeVoucher(
                voucherId = "VCH-2026-09-042",
                studentId = "std_101",
                studentName = "Muhammad Bilal Dawar",
                rollNumber = "24-MED-042",
                monthYear = "September 2026",
                tuitionFee = 3500,
                examinationFee = 0,
                labCharges = 300,
                libraryCharges = 200,
                totalAmount = 4000,
                issueDate = "01 Sep 2026",
                dueDate = "15 Sep 2026",
                isPaid = true,
                paidDate = "11 Sep 2026",
                bankBranch = "National Bank of Pakistan, Mirali Branch"
            ),
            FeeVoucher(
                voucherId = "VCH-2026-08-042",
                studentId = "std_101",
                studentName = "Muhammad Bilal Dawar",
                rollNumber = "24-MED-042",
                monthYear = "August 2026",
                tuitionFee = 3500,
                examinationFee = 0,
                labCharges = 300,
                libraryCharges = 200,
                totalAmount = 4000,
                issueDate = "01 Aug 2026",
                dueDate = "15 Aug 2026",
                isPaid = true,
                paidDate = "09 Aug 2026",
                bankBranch = "Habib Bank Limited, Mirali Cantt Branch"
            )
        )

        // Messages
        _messages.value = listOf(
            MessageItem(
                id = "msg_1",
                threadId = "th_principal",
                senderName = "Prof. Dr. Abdul Qayyum (Principal)",
                senderRole = "Principal",
                recipientName = "Muhammad Bilal & Parents",
                recipientRole = "Student & Parent",
                subject = "Appreciation on Mid-Term Distinction",
                messageText = "Assalam-o-Alaikum Haji Gul Rehman sb. Pleased to inform you that Bilal achieved 2nd position in F.Sc Pre-Med Midterms. Keep up this consistency for the board exams.",
                timestamp = "10 Oct, 11:20 AM",
                isFromUser = false,
                isRead = true
            ),
            MessageItem(
                id = "msg_2",
                threadId = "th_principal",
                senderName = "Haji Gul Rehman (Parent)",
                senderRole = "Parent",
                recipientName = "Prof. Dr. Abdul Qayyum (Principal)",
                recipientRole = "Principal",
                subject = "Re: Appreciation on Mid-Term Distinction",
                messageText = "Shukriya Principal Sahab! We are very grateful for the dedicated guidance of Crescent College faculty in Mirali.",
                timestamp = "10 Oct, 01:15 PM",
                isFromUser = true,
                isRead = true
            ),
            MessageItem(
                id = "msg_3",
                threadId = "th_chem",
                senderName = "Prof. Dr. Abdul Qayyum",
                senderRole = "Chemistry Department",
                recipientName = "Bilal Dawar",
                recipientRole = "Student",
                subject = "Organic Synthesis Lab Assignment",
                messageText = "Please ensure your practical notebook has the esterification reaction equations signed before Monday's review.",
                timestamp = "09 Oct, 03:40 PM",
                isFromUser = false,
                isRead = true
            ),
            MessageItem(
                id = "msg_4",
                threadId = "th_accounts",
                senderName = "Accounts Office (Mr. Shafi)",
                senderRole = "Administration",
                recipientName = "Parent / Student",
                recipientRole = "Parent",
                subject = "October Fee Voucher Generated",
                messageText = "Your October 2026 fee voucher has been published in the portal. You can also view the digital challan copy.",
                timestamp = "02 Oct, 10:00 AM",
                isFromUser = false,
                isRead = true
            )
        )

        // Pre-seeded Admissions
        _admissions.value = listOf(
            AdmissionApplication(
                id = "CSDC-2026-ADM842",
                applicantName = "Zubair Ahmad Dawar",
                fatherName = "Nemat Ullah Khan",
                cnicOrBForm = "12101-7894561-3",
                dateOfBirth = "14 March 2009",
                gender = "Male",
                contactNumber = "+92 302 9871234",
                email = "zubair.dawar@gmail.com",
                address = "Village Hasu Khel, Tehsil Mirali, North Waziristan",
                programApplied = "F.Sc Pre-Medical",
                previousInstitution = "Government High School No. 1 Mirali",
                previousMarks = 982,
                previousTotalMarks = 1100,
                status = ApplicationStatus.UNDER_REVIEW,
                submissionDate = "08 Oct 2026"
            ),
            AdmissionApplication(
                id = "CSDC-2026-ADM843",
                applicantName = "Khadija Bibi Wazir",
                fatherName = "Malik Sher Wali Wazir",
                cnicOrBForm = "12101-4458921-8",
                dateOfBirth = "22 August 2008",
                gender = "Female",
                contactNumber = "+92 307 6543210",
                email = "khadija.wazir@gmail.com",
                address = "Mirali Main Bazar Near Degree College Road",
                programApplied = "ICS Computer Science",
                previousInstitution = "Crescent School Mirali (Girls Wing)",
                previousMarks = 1012,
                previousTotalMarks = 1100,
                status = ApplicationStatus.VERIFIED,
                submissionDate = "05 Oct 2026"
            )
        )

        // Announcements
        _announcements.value = listOf(
            Announcement(
                id = "ann_1",
                title = "BISE Bannu Annual Exam Registration Notice",
                date = "10 Oct 2026",
                category = "Academic Board",
                summary = "Registration forms for Intermediate Annual Examination 2027 are available in the college administration block.",
                fullContent = "All 1st Year and 2nd Year students of Crescent College Mirali must submit verified B-Form and three attested passport size photographs by 25 October 2026.",
                isUrgent = true
            ),
            Announcement(
                id = "ann_2",
                title = "Annual Science Exhibition & STEM Fair 2026",
                date = "07 Oct 2026",
                category = "Co-Curricular",
                summary = "Crescent College will host the North Waziristan inter-school science and robotics exhibition next month.",
                fullContent = "Teams from Physics, Chemistry, Biology and Computer Science departments are invited to submit their project models to Engr. Farhan Ullah by 20 October.",
                isUrgent = false
            ),
            Announcement(
                id = "ann_3",
                title = "College Bus Route Schedule Update for Mirali Sub-Districts",
                date = "04 Oct 2026",
                category = "Transport",
                summary = "New transport pick-and-drop timings for Ippi, Edek, and Hasu Khel routes during morning shifts.",
                fullContent = "Morning college buses will depart from Hasu Khel junction at 07:15 AM sharp and Edek point at 07:25 AM to ensure arrival before the 08:00 AM assembly.",
                isUrgent = false
            )
        )
    }

    fun selectStudent(student: Student) {
        _selectedStudent.value = student
    }

    fun markAttendance(studentId: String, status: AttendanceStatus, subject: String, remarks: String) {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val todayStr = formatter.format(Date())
        val newRecord = AttendanceRecord(
            id = "att_${UUID.randomUUID().toString().take(8)}",
            studentId = studentId,
            date = todayStr,
            status = status,
            subject = subject,
            teacherName = "Faculty (Marked in App)",
            remarks = remarks.ifBlank { "Marked on $todayStr" }
        )
        val currentList = _attendanceRecords.value.toMutableList()
        currentList.add(0, newRecord)
        _attendanceRecords.value = currentList

        // Recalculate attendance % for student
        val studentRecords = currentList.filter { it.studentId == studentId }
        val presentCount = studentRecords.count { it.status == AttendanceStatus.PRESENT || it.status == AttendanceStatus.LATE }
        val percent = if (studentRecords.isNotEmpty()) (presentCount.toFloat() / studentRecords.size) * 100f else 94.0f

        _students.value = _students.value.map { s ->
            if (s.id == studentId) s.copy(attendancePercentage = ((percent * 10).toInt() / 10f)) else s
        }
        if (_selectedStudent.value?.id == studentId) {
            _selectedStudent.value = _selectedStudent.value?.copy(attendancePercentage = ((percent * 10).toInt() / 10f))
        }
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
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dateStr = formatter.format(Date())
        val randomNum = (100..999).random()
        val app = AdmissionApplication(
            id = "CSDC-2026-ADM$randomNum",
            applicantName = applicantName,
            fatherName = fatherName,
            cnicOrBForm = cnicOrBForm,
            dateOfBirth = dob,
            gender = gender,
            contactNumber = phone,
            email = email,
            address = address,
            programApplied = program,
            previousInstitution = prevSchool,
            previousMarks = prevMarks,
            previousTotalMarks = prevTotal,
            status = ApplicationStatus.SUBMITTED,
            submissionDate = dateStr
        )
        val updated = _admissions.value.toMutableList()
        updated.add(0, app)
        _admissions.value = updated

        // Generate automated notification
        addNotification(
            title = "Admission Application Received: ${app.id}",
            message = "New application submitted for ${app.applicantName} ($program). Admission office review in progress.",
            category = NotificationCategory.GENERAL,
            dueDateOrDate = "Submitted: $dateStr",
            priorityBadge = "NEW ADMISSION"
        )

        return app
    }

    fun sendMessage(
        recipientName: String,
        recipientRole: String,
        subject: String,
        messageText: String
    ): MessageItem {
        val formatter = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        val timeStr = formatter.format(Date())
        val userMsg = MessageItem(
            id = "msg_${UUID.randomUUID().toString().take(8)}",
            threadId = "th_${UUID.randomUUID().toString().take(6)}",
            senderName = _selectedStudent.value?.fullName ?: "Parent / Student",
            senderRole = "Student / Parent",
            recipientName = recipientName,
            recipientRole = recipientRole,
            subject = subject,
            messageText = messageText,
            timestamp = timeStr,
            isFromUser = true,
            isRead = true
        )
        val updated = _messages.value.toMutableList()
        updated.add(0, userMsg)
        _messages.value = updated
        return userMsg
    }

    fun payFeeVoucher(voucherId: String) {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val today = formatter.format(Date())
        _feeVouchers.value = _feeVouchers.value.map { v ->
            if (v.voucherId == voucherId) {
                v.copy(isPaid = true, paidDate = today)
            } else v
        }
        val currentSelected = _selectedStudent.value
        if (currentSelected != null) {
            _selectedStudent.value = currentSelected.copy(feeDueAmount = 0, feeDueDate = "Paid (Receipt: $voucherId)")
            _students.value = _students.value.map {
                if (it.id == currentSelected.id) it.copy(feeDueAmount = 0, feeDueDate = "Paid") else it
            }
        }
        addNotification(
            title = "Fee Payment Successful",
            message = "Voucher $voucherId paid successfully for ${_selectedStudent.value?.fullName}. Official receipt recorded in accounts.",
            category = NotificationCategory.FEE,
            dueDateOrDate = "Paid: $today",
            priorityBadge = "FEE PAID"
        )
    }

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    fun markNotificationRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun addNotification(
        title: String,
        message: String,
        category: NotificationCategory,
        dueDateOrDate: String?,
        priorityBadge: String?
    ) {
        val item = NotificationItem(
            id = "notif_${UUID.randomUUID().toString().take(8)}",
            title = title,
            message = message,
            category = category,
            timestamp = "Just now",
            isRead = false,
            dueDateOrDate = dueDateOrDate,
            priorityBadge = priorityBadge
        )
        val current = _notifications.value.toMutableList()
        current.add(0, item)
        _notifications.value = current
    }
}

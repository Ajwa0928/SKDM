package com.example

import com.example.crescent.data.CrescentRepository
import com.example.crescent.model.AttendanceStatus
import com.example.crescent.model.NotificationCategory
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CrescentCollegeRepositoryTest {

    private lateinit var repository: CrescentRepository

    @Before
    fun setUp() {
        repository = CrescentRepository()
    }

    @Test
    fun testInitialSeedDataLoaded() {
        val students = repository.students.value
        val teachers = repository.teachers.value
        val notifications = repository.notifications.value

        assertTrue("Students should be pre-seeded", students.isNotEmpty())
        assertTrue("Teachers should be pre-seeded", teachers.isNotEmpty())
        assertTrue("Notifications should be pre-seeded", notifications.isNotEmpty())

        val selectedStudent = repository.selectedStudent.value
        assertNotNull("Selected student should be initialized", selectedStudent)
        assertEquals("Muhammad Bilal Dawar", selectedStudent?.fullName)
    }

    @Test
    fun testMarkAttendanceUpdatesRate() {
        val student = repository.selectedStudent.value!!
        val initialRecordsCount = repository.attendanceRecords.value.size

        repository.markAttendance(
            studentId = student.id,
            status = AttendanceStatus.PRESENT,
            subject = "Chemistry Lab",
            remarks = "Present on time"
        )

        val updatedRecords = repository.attendanceRecords.value
        assertEquals(initialRecordsCount + 1, updatedRecords.size)
        assertEquals(AttendanceStatus.PRESENT, updatedRecords.first().status)
        assertEquals("Chemistry Lab", updatedRecords.first().subject)
    }

    @Test
    fun testSubmitAdmissionApplicationGeneratesId() {
        val initialCount = repository.admissions.value.size

        val app = repository.submitAdmission(
            applicantName = "Shahid Ullah",
            fatherName = "Gul Nawaz",
            cnicOrBForm = "12101-9988776-5",
            dob = "10 Jan 2009",
            gender = "Male",
            phone = "+92 300 1234567",
            email = "shahid@example.com",
            address = "Mirali Main Bazar",
            program = "F.Sc Pre-Medical",
            prevSchool = "Govt High School Mirali",
            prevMarks = 990,
            prevTotal = 1100
        )

        assertTrue(app.id.startsWith("CSDC-2026-ADM"))
        assertEquals("Shahid Ullah", app.applicantName)
        assertEquals(initialCount + 1, repository.admissions.value.size)
    }

    @Test
    fun testSendMessageAddsToStream() {
        val initialMessagesCount = repository.messages.value.size

        val msg = repository.sendMessage(
            recipientName = "Prof. Dr. Abdul Qayyum",
            recipientRole = "Principal",
            subject = "Leave Application",
            messageText = "Respected Principal, requesting leave for tomorrow due to family emergency."
        )

        assertEquals(initialMessagesCount + 1, repository.messages.value.size)
        assertTrue(msg.isFromUser)
        assertEquals("Leave Application", msg.subject)
    }

    @Test
    fun testPayFeeVoucherClearsBalance() {
        val unpaidVoucher = repository.feeVouchers.value.first { !it.isPaid }
        val voucherId = unpaidVoucher.voucherId

        repository.payFeeVoucher(voucherId)

        val updatedVoucher = repository.feeVouchers.value.first { it.voucherId == voucherId }
        assertTrue("Voucher must be marked as paid", updatedVoucher.isPaid)
        assertNotNull("Payment date must be recorded", updatedVoucher.paidDate)
        assertEquals(0, repository.selectedStudent.value?.feeDueAmount)
    }

    @Test
    fun testAutomatedNotificationsForExamAndFee() {
        val notifs = repository.notifications.value
        val examNotifs = notifs.filter { it.category == NotificationCategory.EXAM }
        val feeNotifs = notifs.filter { it.category == NotificationCategory.FEE }

        assertTrue("Should have automated exam alerts", examNotifs.isNotEmpty())
        assertTrue("Should have automated fee reminders", feeNotifs.isNotEmpty())
    }
}

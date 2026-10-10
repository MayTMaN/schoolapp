package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Attendance;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.AttendanceRepository;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.EnrollmentRepository;
import com.organization.schoolapp.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    StudentRepository studentRepo;
    @Mock
    CourseRepository courseRepo;
    @Mock
    AttendanceRepository attendanceRepo;
    @Mock
    EnrollmentRepository enrollmentRepo;
    @InjectMocks
    AttendanceService attendanceService;

    @Test
    void markAttendance_alreadyMarked_throwsException() {
        LocalDate date = LocalDate.of(2026,10,8);

        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);
        when(attendanceRepo.findAttendance(1,1,date)).thenReturn(new Attendance(course, student, date, true));

        assertThrows(IllegalArgumentException.class, () -> attendanceService.markAttendance(true,1,1, date));

        verify(attendanceRepo, never()).save(anyBoolean(),anyInt(),anyInt(),any());
    }

    @Test
    void markAttendance_notMarked_isSaved() {
        LocalDate date = LocalDate.of(2026,10,8);

        givenStudentAndCourseExist(1,1);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);

        attendanceService.markAttendance(true,1,1,date);

        verify(attendanceRepo).save(true,1,1,date);
    }

    @Test
    void changeAttendance_attendanceDoesntExist_throwsException() {
        LocalDate date = LocalDate.of(2026,10,8);
        givenStudentAndCourseExist(1,1);

        assertThrows(IllegalStateException.class, () -> attendanceService.changeAttendance(true,1,1,date));

        verify(attendanceRepo, never()).update(anyBoolean(),anyInt(),anyInt(),any());
    }

    @Test
    void changeAttendance_attendanceExists_isUpdated() {
        LocalDate date = LocalDate.of(2026,10,8);

        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(attendanceRepo.findAttendance(1,1,date)).thenReturn(new Attendance(course, student, date, true));

        attendanceService.changeAttendance(false,1,1,date);

        verify(attendanceRepo).update(false,1,1,date);
    }

    @Test
    void removeAttendance_attendanceDoesntExist_throwsException() {
        LocalDate date = LocalDate.of(2026,10,8);
        givenStudentAndCourseExist(1,1);

        assertThrows(IllegalStateException.class, () -> attendanceService.removeAttendance(1,1,date));

        verify(attendanceRepo, never()).delete(anyInt(),anyInt(),any());
    }

    @Test
    void removeAttendance_attendanceExists_isRemoved() {
        LocalDate date = LocalDate.of(2026,10,8);

        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(attendanceRepo.findAttendance(1,1,date)).thenReturn(new Attendance(course, student, date, true));

        attendanceService.removeAttendance(1,1,date);

        verify(attendanceRepo).delete(1,1,date);
    }

    @Test
    void getAttendancePercentage_noAttendanceRecorded_throwsException() {
        givenStudentAndCourseExist(1,1);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> attendanceService.getAttendancePercentage(1,1));
    }

    @Test
    void getAttendancePercentage_twoMarks_returnsPercentage() {
        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);
        when(attendanceRepo.findStudentsAttendance(1,1)).thenReturn(List.of(new Attendance(course, student, LocalDate.of(2026,10,5), true), new Attendance(course,student,LocalDate.of(2026,10,4), false)));

        double percentage = attendanceService.getAttendancePercentage(1,1);

        assertEquals(50, percentage);
    }

    @Test
    void getAttendancePercentage_attendedAll_returns100() {
        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);
        when(attendanceRepo.findStudentsAttendance(1,1)).thenReturn(List.of(new Attendance(course, student, LocalDate.of(2026,10,5), true)));
        double percentage = attendanceService.getAttendancePercentage(1,1);

        assertEquals(100, percentage);
    }

    @Test
    void getAttendancePercentage_missedAll_returns0() {
        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);
        when(attendanceRepo.findStudentsAttendance(1,1)).thenReturn(List.of(new Attendance(course, student, LocalDate.of(2026,10,5), false)));
        double percentage = attendanceService.getAttendancePercentage(1,1);

        assertEquals(0, percentage);
    }

    @Test
    void getAttendance_attendanceExists_returnsAttendance() {
        LocalDate date = LocalDate.of(2026,10,8);
        Student student = new Student(1, "Bob");
        Course course = new Course(1, "Math", new Teacher(1, "Smith"));
        Attendance expectedAttendance = new Attendance(course, student, date, true);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(attendanceRepo.findAttendance(1,1,date)).thenReturn(expectedAttendance);

        Attendance actualAttendance = attendanceService.getAttendance(1,1,date);

        assertEquals(expectedAttendance, actualAttendance);
    }

    @Test
    void getAttendance_notMarked_throwsException() {
        LocalDate date = LocalDate.of(2026,10,8);
        givenStudentAndCourseExist(1,1);

        assertThrows(IllegalStateException.class, () -> attendanceService.getAttendance(1,1,date));
    }

    @Test
    void getAttendance_dateInFuture_throwsException() {
        LocalDate future = LocalDate.now().plusDays(2);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> attendanceService.getAttendance(1,1,future));

        assertEquals("Date can't be in the future", e.getMessage());
    }

    @Test
    void getStudentAttendance_twoRecords_returnsRecords() {
        Student student = new Student(1, "Bob");
        Course course = new Course(1, "Math", new Teacher(1, "Smith"));
        List<Attendance> expectedAttendance = List.of(
                new Attendance(course, student, LocalDate.of(2026,10,7), true),
                new Attendance(course, student, LocalDate.of(2026,10,8), false));

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(attendanceRepo.findStudentsAttendance(1,1)).thenReturn(expectedAttendance);

        List<Attendance> actualAttendance = attendanceService.getStudentAttendance(1,1);

        assertEquals(expectedAttendance, actualAttendance);
    }

    @Test
    void getStudentAttendance_studentOrCourseDoesntExist_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> attendanceService.getStudentAttendance(1,1));

        assertEquals("Incorrect student or course", e.getMessage());
    }

    @Test
    void getClassAttendance_courseExists_returnsClassList() {
        LocalDate date = LocalDate.of(2026,10,8);
        Course course = new Course(1, "Math", new Teacher(1, "Smith"));
        List<Attendance> expectedClassList = List.of(
                new Attendance(course, new Student(1, "Bob"), date, true),
                new Attendance(course, new Student(2, "Jane"), date, false));

        when(courseRepo.findById(1)).thenReturn(course);
        when(attendanceRepo.findCourseAttendance(1,date)).thenReturn(expectedClassList);

        List<Attendance> actualClassList = attendanceService.getClassAttendance(1,date);

        assertEquals(expectedClassList, actualClassList);
    }

    @Test
    void getClassAttendance_courseDoesntExist_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> attendanceService.getClassAttendance(1,LocalDate.of(2026,10,8)));

        assertEquals("No course with id 1", e.getMessage());
    }

    @Test
    void getClassAttendance_dateIsNull_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> attendanceService.getClassAttendance(1,null));

        assertEquals("Date not provided", e.getMessage());
    }

    private void givenStudentAndCourseExist(int courseId, int studentId) {
        Student student = new Student(studentId, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(courseId, "Math", teacher);

        when(studentRepo.findById(studentId)).thenReturn(student);
        when(courseRepo.findById(courseId)).thenReturn(course);
    }
}

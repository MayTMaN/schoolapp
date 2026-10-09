package com.organization.schoolapp.service;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    StudentRepository studentRepo;
    @Mock
    CourseRepository courseRepo;
    @Mock
    EnrollmentRepository enrollmentRepo;
    @InjectMocks
    EnrollmentService enrollmentService;

    @Test
    void enroll_studentEnrolled_throwsException() {
        givenStudentAndCourseExist(1,1);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> enrollmentService.enrollStudent(1,1));

        verify(enrollmentRepo, never()).enroll(anyInt(),anyInt());
    }

    @Test
    void enroll_studentNotEnrolled_isEnrolled() {
        givenStudentAndCourseExist(1,1);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(false);

        enrollmentService.enrollStudent(1,1);

        verify(enrollmentRepo).enroll(1,1);
    }

    @Test
    void dismiss_studentNotEnrolled_throwsException() {
        givenStudentAndCourseExist(1,1);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> enrollmentService.dismissStudent(1,1));

        verify(enrollmentRepo, never()).dismiss(anyInt(),anyInt());
    }

    @Test
    void dismiss_studentEnrolled_isDismissed() {
        givenStudentAndCourseExist(1,1);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);

        enrollmentService.dismissStudent(1,1);

        verify(enrollmentRepo).dismiss(1,1);
    }

    @Test
    void getStudentsByCourse_courseDoesntExist_throwsException() {

        assertThrows(IllegalArgumentException.class, () -> enrollmentService.getStudentsByCourse(1));

    }

    @Test
    void getStudentsByCourse_courseIsEmpty_returnsEmptyList() {
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);
        when(courseRepo.findById(1)).thenReturn(course);

        List<Student> expectedList = List.of();
        List<Student> actualList = enrollmentService.getStudentsByCourse(1);

        assertEquals(expectedList,actualList);
    }

    @Test
    void getStudentsByCourse_TwoStudents_returnsTwoStudents() {
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);
        when(courseRepo.findById(1)).thenReturn(course);

        List<Student> expectedList = List.of(new Student(1,"John"), new Student(2,"Jane"));
        when(enrollmentRepo.findStudentsByCourse(1)).thenReturn(expectedList);

        List<Student> actualList = enrollmentService.getStudentsByCourse(1);

        assertEquals(expectedList, actualList);
    }

    private void givenStudentAndCourseExist(int courseId, int studentId) {
        Student student = new Student(studentId, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(courseId, "Math", teacher);

        when(studentRepo.findById(studentId)).thenReturn(student);
        when(courseRepo.findById(courseId)).thenReturn(course);
    }
}

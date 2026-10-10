package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Grade;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.EnrollmentRepository;
import com.organization.schoolapp.repository.GradeRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GradeServiceTest {

    @Mock
    StudentRepository studentRepo;
    @Mock
    CourseRepository courseRepo;
    @Mock
    EnrollmentRepository enrollmentRepo;
    @Mock
    GradeRepository gradeRepo;
    @InjectMocks
    GradeService gradeService;

    @Test
    void gradeStudent_gradeAbove10_throwsException() {
        lenient().when(studentRepo.findById(1)).thenReturn(new Student(1,"Bob"));
        lenient().when(courseRepo.findById(1)).thenReturn(new Course(1, "Math", new Teacher("Bob")));
        lenient().when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> gradeService.gradeStudent(11, 1, 1, LocalDate.now()));
        verify(gradeRepo, never()).setGrade(anyDouble(),anyInt(),anyInt(),any());

    }

    @Test
    void gradeStudent_gradeExactly10_isSaved() {
        LocalDate date = LocalDate.of(2026,10,8);

        givenStudentAndCourseExist(1,1);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);

        gradeService.gradeStudent(10,1,1,date);

        verify(gradeRepo).setGrade(10,1,1,date);

    }

    @Test
    void changeGrade_gradeExists_isUpdated() {
        LocalDate date = LocalDate.of(2026,10,8);
        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(gradeRepo.findGrade(1,1,date)).thenReturn(new Grade(student, course, 10, date));

        gradeService.changeGrade(5,1,1,date);

        verify(gradeRepo).updateGrade(1,1,date,5);
    }

    @Test
    void changeGrade_gradeDoesntExist_throwsException() {
        LocalDate date = LocalDate.of(2026,10,8);

        givenStudentAndCourseExist(1,1);

        assertThrows(IllegalStateException.class, () -> gradeService.changeGrade(5,1,1,date));

        verify(gradeRepo, never()).updateGrade(anyInt(),anyInt(),any(),anyDouble());
    }

    @Test
    void removeGrade_gradeExists_isRemoved() {
        LocalDate date = LocalDate.of(2026,10,8);
        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);

        when(gradeRepo.findGrade(1,1,date)).thenReturn(new Grade(student, course, 10, date));

        gradeService.removeGrade(1,1,date);

        verify(gradeRepo).removeGrade(1,1,date);
    }

    @Test
    void removeGrade_gradeDoesntExist_throwsException() {
        LocalDate date = LocalDate.of(2026,10,8);

        givenStudentAndCourseExist(1,1);

        assertThrows(IllegalStateException.class, () -> gradeService.removeGrade(1,1,date));

        verify(gradeRepo, never()).removeGrade(anyInt(),anyInt(),any());
    }

    @Test
    void getAverage_twoGrades_returnsAverage() {
        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);
        when(gradeRepo.findStudentGrades(1,1)).thenReturn(List.of(new Grade(student,course,6,LocalDate.now()), new Grade(student,course,9,LocalDate.now())));

        double average = gradeService.getAverage(1,1);

        assertEquals(7.5, average);
    }

    @Test
    void getAverage_oneGrade_returnsSameNumber() {
        Student student = new Student(1, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(1, "Math", teacher);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(enrollmentRepo.isEnrolled(1,1)).thenReturn(true);
        when(gradeRepo.findStudentGrades(1,1)).thenReturn(List.of(new Grade(student,course,10,LocalDate.now())));

        double average = gradeService.getAverage(1,1);

        assertEquals(10, average);
    }

    @Test
    void getGrade_gradeExists_returnsGrade() {
        LocalDate date = LocalDate.of(2026,10,8);
        Student student = new Student(1, "Bob");
        Course course = new Course(1, "Math", new Teacher(1, "Smith"));
        Grade expectedGrade = new Grade(student, course, 8, date);

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(gradeRepo.findGrade(1,1,date)).thenReturn(expectedGrade);

        Grade actualGrade = gradeService.getGrade(1,1,date);

        assertEquals(expectedGrade, actualGrade);
    }

    @Test
    void getGrade_gradeDoesntExist_throwsException() {
        LocalDate date = LocalDate.of(2026,10,8);
        givenStudentAndCourseExist(1,1);

        assertThrows(IllegalStateException.class, () -> gradeService.getGrade(1,1,date));
    }

    @Test
    void getGrade_studentOrCourseDoesntExist_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> gradeService.getGrade(1,1,LocalDate.of(2026,10,8)));

        assertEquals("Incorrect student or course", e.getMessage());
    }

    @Test
    void getStudentGrades_twoGrades_returnsGrades() {
        Student student = new Student(1, "Bob");
        Course course = new Course(1, "Math", new Teacher(1, "Smith"));
        List<Grade> expectedGrades = List.of(new Grade(student, course, 6, LocalDate.of(2026,10,7)), new Grade(student, course, 9, LocalDate.of(2026,10,8)));

        when(studentRepo.findById(1)).thenReturn(student);
        when(courseRepo.findById(1)).thenReturn(course);
        when(gradeRepo.findStudentGrades(1,1)).thenReturn(expectedGrades);

        List<Grade> actualGrades = gradeService.getStudentGrades(1,1);

        assertEquals(expectedGrades, actualGrades);
    }

    @Test
    void getStudentGrades_noGrades_returnsEmptyList() {
        givenStudentAndCourseExist(1,1);

        List<Grade> actualGrades = gradeService.getStudentGrades(1,1);

        assertTrue(actualGrades.isEmpty());
    }

    @Test
    void getStudentGrades_studentOrCourseDoesntExist_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> gradeService.getStudentGrades(1,1));

        assertEquals("Incorrect student or course", e.getMessage());
    }

    @Test
    void getCourseGrades_courseExists_returnsGrades() {
        Course course = new Course(1, "Math", new Teacher(1, "Smith"));
        List<Grade> expectedGrades = List.of(
                new Grade(new Student(1, "Bob"), course, 6, LocalDate.of(2026,10,8)),
                new Grade(new Student(2, "Jane"), course, 9, LocalDate.of(2026,10,8)));

        when(courseRepo.findById(1)).thenReturn(course);
        when(gradeRepo.findGradesByCourse(1)).thenReturn(expectedGrades);

        List<Grade> actualGrades = gradeService.getCourseGrades(1);

        assertEquals(expectedGrades, actualGrades);
    }

    @Test
    void getCourseGrades_courseDoesntExist_throwsException() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> gradeService.getCourseGrades(1));

        assertEquals("No course with id 1", e.getMessage());
    }

    private void givenStudentAndCourseExist(int courseId, int studentId) {
        Student student = new Student(studentId, "Bob");
        Teacher teacher = new Teacher(1, "Smith");
        Course course = new Course(courseId, "Math", teacher);

        when(studentRepo.findById(studentId)).thenReturn(student);
        when(courseRepo.findById(courseId)).thenReturn(course);
    }
}

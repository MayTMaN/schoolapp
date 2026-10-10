package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    CourseRepository courseRepo;
    @Mock
    TeacherRepository teacherRepo;
    @InjectMocks
    CourseService courseService;

    @Test
    void addCourse_nameIsNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse(null, 1));

        verify(courseRepo, never()).save(any());
    }

    @Test
    void addCourse_nameIsBlank_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse("  ", 1));

        verify(courseRepo, never()).save(any());
    }

    @Test
    void addCourse_teacherDoesntExist_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse("Math", 1));

        verify(courseRepo, never()).save(any());
    }

    @Test
    void addCourse_nameAndTeacherGiven_isSaved() {
        Teacher teacher = new Teacher(1, "Smith");
        when(teacherRepo.findById(1)).thenReturn(teacher);
        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);

        courseService.addCourse("Math", 1);

        verify(courseRepo).save(captor.capture());
        assertEquals("Math", captor.getValue().getCourseName());
        assertEquals(teacher, captor.getValue().getTeacher());
    }

    @Test
    void getCourse_courseExists_returnsCourse() {
        Course expectedCourse = new Course(1, "Math", new Teacher(1, "Smith"));
        when(courseRepo.findById(1)).thenReturn(expectedCourse);

        Course actualCourse = courseService.getCourse(1);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void getCourse_courseDoesntExist_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> courseService.getCourse(1));
    }

    @Test
    void getAllCourses_twoCourses_returnsTwoCourses() {
        Teacher teacher = new Teacher(1, "Smith");
        List<Course> expectedCourses = List.of(new Course(1, "Math", teacher), new Course(2, "History", teacher));
        when(courseRepo.findAll()).thenReturn(expectedCourses);

        List<Course> actualCourses = courseService.getAllCourses();

        assertEquals(expectedCourses, actualCourses);
    }

    @Test
    void renameCourse_nameIsBlank_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> courseService.renameCourse(1, "  "));

        verify(courseRepo, never()).update(any());
    }

    @Test
    void renameCourse_courseDoesntExist_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> courseService.renameCourse(1, "Algebra"));

        verify(courseRepo, never()).update(any());
    }

    @Test
    void renameCourse_courseExists_isRenamedAndKeepsTeacher() {
        Teacher teacher = new Teacher(1, "Smith");
        when(courseRepo.findById(1)).thenReturn(new Course(1, "Math", teacher));
        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);

        courseService.renameCourse(1, "  Algebra  ");

        verify(courseRepo).update(captor.capture());
        assertEquals("Algebra", captor.getValue().getCourseName());
        assertEquals(teacher, captor.getValue().getTeacher());
    }

    @Test
    void changeTeacher_courseDoesntExist_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> courseService.changeTeacher(1, 2));

        verify(courseRepo, never()).update(any());
    }

    @Test
    void changeTeacher_newTeacherDoesntExist_throwsException() {
        when(courseRepo.findById(1)).thenReturn(new Course(1, "Math", new Teacher(1, "Smith")));

        assertThrows(IllegalArgumentException.class, () -> courseService.changeTeacher(1, 2));

        verify(courseRepo, never()).update(any());
    }

    @Test
    void changeTeacher_courseAndTeacherExist_isUpdatedAndKeepsName() {
        when(courseRepo.findById(1)).thenReturn(new Course(1, "Math", new Teacher(1, "Smith")));
        Teacher newTeacher = new Teacher(2, "Jones");
        when(teacherRepo.findById(2)).thenReturn(newTeacher);
        ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);

        courseService.changeTeacher(1, 2);

        verify(courseRepo).update(captor.capture());
        assertEquals(newTeacher, captor.getValue().getTeacher());
        assertEquals("Math", captor.getValue().getCourseName());
    }

    @Test
    void deleteCourse_courseDoesntExist_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> courseService.deleteCourse(1));

        verify(courseRepo, never()).delete(anyInt());
    }

    @Test
    void deleteCourse_courseExists_isDeleted() {
        when(courseRepo.findById(1)).thenReturn(new Course(1, "Math", new Teacher(1, "Smith")));

        courseService.deleteCourse(1);

        verify(courseRepo).delete(1);
    }
}

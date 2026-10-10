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
public class TeacherServiceTest {

    @Mock
    TeacherRepository teacherRepo;
    @Mock
    CourseRepository courseRepo;
    @InjectMocks
    TeacherService teacherService;

    @Test
    void addTeacher_nameIsNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> teacherService.addTeacher(null));

        verify(teacherRepo, never()).save(any());
    }

    @Test
    void addTeacher_nameIsBlank_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> teacherService.addTeacher("  "));

        verify(teacherRepo, never()).save(any());
    }

    @Test
    void addTeacher_nameGiven_isSaved() {
        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);

        teacherService.addTeacher("Smith");

        verify(teacherRepo).save(captor.capture());
        assertEquals("Smith", captor.getValue().getName());
    }

    @Test
    void getTeacher_teacherExists_returnsTeacher() {
        Teacher expectedTeacher = new Teacher(1, "Smith");
        when(teacherRepo.findById(1)).thenReturn(expectedTeacher);

        Teacher actualTeacher = teacherService.getTeacher(1);

        assertEquals(expectedTeacher, actualTeacher);
    }

    @Test
    void getTeacher_teacherDoesntExist_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacher(1));
    }

    @Test
    void getAllTeachers_twoTeachers_returnsTwoTeachers() {
        List<Teacher> expectedTeachers = List.of(new Teacher(1, "Smith"), new Teacher(2, "Jones"));
        when(teacherRepo.findAll()).thenReturn(expectedTeachers);

        List<Teacher> actualTeachers = teacherService.getAllTeachers();

        assertEquals(expectedTeachers, actualTeachers);
    }

    @Test
    void deleteTeacher_teacherDoesntExist_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> teacherService.deleteTeacher(1));

        verify(teacherRepo, never()).delete(anyInt());
    }

    @Test
    void deleteTeacher_teacherStillTeachesCourse_throwsException() {
        Teacher teacher = new Teacher(1, "Smith");
        when(teacherRepo.findById(1)).thenReturn(teacher);
        when(courseRepo.findAll()).thenReturn(List.of(new Course(1, "Math", teacher)));

        assertThrows(IllegalStateException.class, () -> teacherService.deleteTeacher(1));

        verify(teacherRepo, never()).delete(anyInt());
    }

    @Test
    void deleteTeacher_teachesNoCourses_isDeleted() {
        when(teacherRepo.findById(1)).thenReturn(new Teacher(1, "Smith"));

        teacherService.deleteTeacher(1);

        verify(teacherRepo).delete(1);
    }
}

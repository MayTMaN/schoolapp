package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    StudentRepository studentRepo;
    @InjectMocks
    StudentService studentService;

    @Test
    void addStudent_nameIsNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> studentService.addStudent(null));

        verify(studentRepo, never()).save(any());
    }

    @Test
    void addStudent_nameIsBlank_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> studentService.addStudent("  "));

        verify(studentRepo, never()).save(any());
    }

    @Test
    void addStudent_nameGiven_isSaved() {
        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);

        studentService.addStudent("Bob");

        verify(studentRepo).save(captor.capture());
        assertEquals("Bob", captor.getValue().getName());
    }

    @Test
    void getStudent_studentExists_returnsStudent() {
        Student expectedStudent = new Student(1, "Bob");

        when(studentRepo.findById(1)).thenReturn(expectedStudent);

        Student actualStudent = studentService.getStudent(1);

        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void getStudent_studentIsNull_throwsException() {
        when(studentRepo.findById(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> studentService.getStudent(1));
    }

    @Test
    void getAllStudents_twoStudents_returnsTwoStudents() {
        List<Student> expectedStudents = List.of(new Student(1,"Bob"), new Student(2, "John"));

        when(studentRepo.findAll()).thenReturn(expectedStudents);

        List<Student> actualStudents = studentService.getAllStudents();

        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    void deleteStudent_studentDoesntExist_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> studentService.deleteStudent(1));

        verify(studentRepo, never()).delete(anyInt());
    }

    @Test
    void deleteStudent_studentExists_isDeleted() {
        when(studentRepo.findById(1)).thenReturn(new Student(1, "Bob"));

        studentService.deleteStudent(1);

        verify(studentRepo).delete(1);
    }

}

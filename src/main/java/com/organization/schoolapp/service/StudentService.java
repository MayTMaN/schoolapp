package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepo;

    public StudentService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public void addStudent(String name) {
        checkName(name);

        studentRepo.save(new Student(name.trim()));
    }

    public Student getStudent(int studentId) {
        return findExistingStudent(studentId);
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public void deleteStudent(int studentId) {
        findExistingStudent(studentId);

        studentRepo.delete(studentId);
    }

    private void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Student name can't be empty");
        }
    }

    private Student findExistingStudent(int studentId) {
        return studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("No student with id " + studentId));
    }
}

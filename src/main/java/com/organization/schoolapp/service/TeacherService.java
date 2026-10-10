package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.TeacherRepository;

import java.util.List;

public class TeacherService {
    private final TeacherRepository teacherRepo;
    private final CourseRepository courseRepo;

    public TeacherService(TeacherRepository teacherRepo, CourseRepository courseRepo) {
        this.teacherRepo = teacherRepo;
        this.courseRepo = courseRepo;
    }

    public void addTeacher(String name) {
        checkName(name);

        teacherRepo.save(new Teacher(name.trim()));
    }

    public Teacher getTeacher(int teacherId) {
        return findExistingTeacher(teacherId);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }

    public void deleteTeacher(int teacherId) {
        findExistingTeacher(teacherId);

        if (teachesAnyCourse(teacherId)) {
            throw new IllegalStateException("Teacher still teaches courses. Assign them to another teacher first");
        }

        teacherRepo.delete(teacherId);
    }

    private boolean teachesAnyCourse(int teacherId) {
        for (Course course : courseRepo.findAll()) {
            if (course.getTeacher().getId() == teacherId) {
                return true;
            }
        }

        return false;
    }

    private void checkName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Teacher name can't be empty");
        }
    }

    private Teacher findExistingTeacher(int teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId);

        if (teacher == null) {
            throw new IllegalArgumentException("No teacher with id " + teacherId);
        }

        return teacher;
    }
}

package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Teacher;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.TeacherRepository;

import java.util.List;

public class CourseService {
    private final CourseRepository courseRepo;
    private final TeacherRepository teacherRepo;

    public CourseService(CourseRepository courseRepo, TeacherRepository teacherRepo) {
        this.courseRepo = courseRepo;
        this.teacherRepo = teacherRepo;
    }

    public void addCourse(String courseName, int teacherId) {
        checkName(courseName);
        Teacher teacher = findExistingTeacher(teacherId);

        courseRepo.save(new Course(courseName.trim(), teacher));
    }

    public Course getCourse(int courseId) {
        return findExistingCourse(courseId);
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public void renameCourse(int courseId, String newName) {
        checkName(newName);
        Course course = findExistingCourse(courseId);

        course.setCourseName(newName.trim());
        courseRepo.update(course);
    }

    public void changeTeacher(int courseId, int newTeacherId) {
        Course course = findExistingCourse(courseId);
        Teacher newTeacher = findExistingTeacher(newTeacherId);

        course.setTeacher(newTeacher);
        courseRepo.update(course);
    }

    public void deleteCourse(int courseId) {
        findExistingCourse(courseId);

        courseRepo.delete(courseId);
    }

    private void checkName(String courseName) {
        if (courseName == null || courseName.isBlank()) {
            throw new IllegalArgumentException("Course name can't be empty");
        }
    }

    private Course findExistingCourse(int courseId) {
        Course course = courseRepo.findById(courseId);

        if (course == null) {
            throw new IllegalArgumentException("No course with id " + courseId);
        }

        return course;
    }

    private Teacher findExistingTeacher(int teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId);

        if (teacher == null) {
            throw new IllegalArgumentException("No teacher with id " + teacherId);
        }

        return teacher;
    }
}

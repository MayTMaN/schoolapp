package com.organization.schoolapp.service;

import com.organization.schoolapp.entity.Attendance;
import com.organization.schoolapp.entity.Course;
import com.organization.schoolapp.entity.Student;
import com.organization.schoolapp.repository.AttendanceRepository;
import com.organization.schoolapp.repository.CourseRepository;
import com.organization.schoolapp.repository.EnrollmentRepository;
import com.organization.schoolapp.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

public class AttendanceService {
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final AttendanceRepository attendanceRepo;
    private final EnrollmentRepository enrollmentRepo;

    public AttendanceService (StudentRepository studentRepo, CourseRepository courseRepo, AttendanceRepository attendanceRepo, EnrollmentRepository enrollmentRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.attendanceRepo = attendanceRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    public void markAttendance (boolean isPresent, int courseId, int studentId, LocalDate date) {

        checkDate(date);
        checkStudentAndCourseExist(courseId, studentId);
        checkEnrolled(courseId, studentId);

        if (attendanceRepo.findAttendance(courseId, studentId, date) != null) {
            throw new IllegalArgumentException("Attendance was already marked on this date");
        }

        attendanceRepo.save(isPresent, courseId, studentId, date);
    }

    public void changeAttendance (boolean isPresent, int courseId, int studentId, LocalDate date) {
        checkDate(date);
        checkStudentAndCourseExist(courseId, studentId);

        if(attendanceRepo.findAttendance(courseId,studentId,date) == null) {
            throw new IllegalStateException("Attendance hasn't been marked");
        }

        attendanceRepo.update(isPresent, courseId, studentId, date);
    }

    public void removeAttendance (int courseId, int studentId, LocalDate date) {
        checkDate(date);
        checkStudentAndCourseExist(courseId, studentId);

        if(attendanceRepo.findAttendance(courseId,studentId,date) == null) {
            throw new IllegalStateException("Attendance hasn't been marked");
        }

        attendanceRepo.delete(courseId, studentId, date);
    }

    public double getAttendancePercentage (int courseId, int studentId) {
        checkStudentAndCourseExist(courseId, studentId);
        checkEnrolled(courseId, studentId);

        List<Attendance> attendanceList = attendanceRepo.findStudentsAttendance(courseId, studentId);

        if(attendanceList.isEmpty()) {
            throw new IllegalStateException("No attendance recorded for this student yet");
        }

        int attended = 0;

        for (Attendance a : attendanceList) {
            if(a.isPresent()) {
                attended++;
            }
        }

        double attendancePercentage = attended * 100.0 / attendanceList.size();
        return attendancePercentage;
    }

    private void checkDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date not provided");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date can't be in the future");
        }
    }

    private void checkStudentAndCourseExist (int courseId, int studentId) {
        Student student = studentRepo.findById(studentId);
        Course course = courseRepo.findById(courseId);

        if (student == null || course == null) {
            throw new IllegalArgumentException("Incorrect student or course");
        }
    }

    private void checkEnrolled (int courseId, int studentId) {
        if (!enrollmentRepo.isEnrolled(courseId, studentId)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }
    }

}

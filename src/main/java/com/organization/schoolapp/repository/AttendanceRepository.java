package com.organization.schoolapp.repository;

import com.organization.schoolapp.entity.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {

    void save(boolean isPresent, int courseId, int studentId, LocalDate date);

    void delete(int courseId, int studentId, LocalDate date);

    void update(boolean isPresent, int courseId, int studentId, LocalDate date);

    Attendance findAttendance(int courseId, int studentId, LocalDate date);

    List<Attendance> findStudentsAttendance(int courseId, int studentId);

    List<Attendance> findCourseAttendance(int courseId, LocalDate date);
}

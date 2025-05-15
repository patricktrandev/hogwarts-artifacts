package com.cloudinfo.hogwartsartifact.course;

import com.cloudinfo.hogwartsartifact.course.dto.CourseDto;

import java.util.List;

public interface CourseService {
    Course createCourse(Course course);

    Course findCourseById(String id);
    List<Course> findAllCourses();
}

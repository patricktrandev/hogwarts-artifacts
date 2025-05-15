package com.cloudinfo.hogwartsartifact.course;

import com.cloudinfo.hogwartsartifact.course.dto.CourseDto;
import com.cloudinfo.hogwartsartifact.exception.ResourceAlreadyExistException;
import com.cloudinfo.hogwartsartifact.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;


    @Override
    public Course createCourse(Course course) {
        Boolean found= courseRepository.existsByName(course.getName());
        if(found){
            throw new ResourceAlreadyExistException("Name", course.getName());
        }
        Course saved=courseRepository.save(course);
        return saved;
    }

    @Override
    public Course findCourseById(String id) {
        Course course=courseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
        return course;
    }

    @Override
    public List<Course> findAllCourses() {
        List<Course> courseList=courseRepository.findAll();
        return courseList;
    }
}

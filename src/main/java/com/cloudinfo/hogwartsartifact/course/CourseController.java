package com.cloudinfo.hogwartsartifact.course;

import com.cloudinfo.hogwartsartifact.course.converter.CourseDtoMapper;
import com.cloudinfo.hogwartsartifact.course.converter.CourseMapper;
import com.cloudinfo.hogwartsartifact.course.dto.CourseDto;
import com.cloudinfo.hogwartsartifact.system.Response;
import com.cloudinfo.hogwartsartifact.system.StatusCode;
import com.cloudinfo.hogwartsartifact.wizard.Wizard;
import com.cloudinfo.hogwartsartifact.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final CourseDtoMapper courseDtoMapper;
    @GetMapping
    public ResponseEntity<Response> findAllWizards(){
        List<CourseDto> courseDtos= courseService.findAllCourses().stream()
                .map(courseMapper::convert).collect(Collectors.toList());

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Find all courses successfully",courseDtos), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> findCourseById(@PathVariable String id){
        Course course=courseService.findCourseById(id);
        CourseDto result= courseMapper.convert(course);

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Find one course successfully", result), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response> createCourse(@Valid @RequestBody CourseDto courseDto){
        Course course= courseDtoMapper.convert(courseDto);
        Course saved=courseService.createCourse(course);
         CourseDto result= courseMapper.convert(saved);
        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Create course successfully", result), HttpStatus.CREATED);
    }

}

package com.cloudinfo.hogwartsartifact.course.converter;

import com.cloudinfo.hogwartsartifact.course.Course;
import com.cloudinfo.hogwartsartifact.course.dto.CourseDto;
import com.cloudinfo.hogwartsartifact.wizard.converter.WizardDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseDtoMapper implements Converter<CourseDto, Course> {
    private final WizardDtoMapper wizardMapper;
    @Override
    public Course convert(CourseDto source) {
        Course course= new Course();
        course.setId(source.id());
        course.setName(source.name());
        course.setStartDate(source.startDate());
        course.setEndDate(source.endDate());
        course.setStatus(source.status());
        course.setWizards(source.wizards()!=null? source.wizards().stream().map(wizardMapper::convert).collect(Collectors.toSet()):null);
        return course;
    }
}

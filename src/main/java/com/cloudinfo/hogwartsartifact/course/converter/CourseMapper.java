package com.cloudinfo.hogwartsartifact.course.converter;

import com.cloudinfo.hogwartsartifact.course.Course;
import com.cloudinfo.hogwartsartifact.course.dto.CourseDto;
import com.cloudinfo.hogwartsartifact.wizard.converter.WizardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseMapper implements Converter<Course, CourseDto> {
    private final WizardMapper wizardMapper;

    @Override
    public CourseDto convert(Course source) {

        CourseDto courseDto= new CourseDto(String.valueOf(source.getId()),
                source.getName(),
                source.getStartDate(),
                source.getEndDate(),
                source.getStatus(),
                source.getWizards()!=null?source.getWizards().stream()
                        .map(wizardMapper::convert)
                        .collect(Collectors.toSet()):null);

        return courseDto;
    }


}

package com.cloudinfo.hogwartsartifact.course.dto;

import com.cloudinfo.hogwartsartifact.wizard.dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

public record CourseDto(String id,
                        @NotEmpty(message = "Course name is required")
                        String name,
                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                        LocalDate startDate,
                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                        LocalDate endDate, Integer status, Set<WizardDto> wizards) {
}

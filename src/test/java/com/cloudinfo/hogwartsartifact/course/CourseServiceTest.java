package com.cloudinfo.hogwartsartifact.course;

import com.cloudinfo.hogwartsartifact.exception.ResourceAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenCourse_whenCreateCourse_thenReturn200Success() {
        Course c= new Course();
        c.setName("test course");
        c.setId(UUID.fromString("473a1ec4-a336-4592-a62f-290e828b4148"));
        c.setStartDate(LocalDate.parse("2025-05-11"));
        c.setEndDate(LocalDate.parse("2025-08-11"));
        c.setStatus(1);
        given(courseRepository.existsByName(c.getName())).willReturn(false);
        given(courseRepository.save(c)).willReturn(c);

        Course saved=courseService.createCourse(c);
//        assertThrows(ResourceAlreadyExistException.class, ()->{
//            wizardService.createWizard(w) ;
//        });
        assertThat(saved.getId()).isEqualTo(c.getId());
        assertThat(saved.getName()).isEqualTo(c.getName());
        verify(courseRepository, times(1)).existsByName(c.getName());

    }
    @Test
    void givenCourse_whenCreateCourse_thenReturn400AlreadyExist() {
        Course c= new Course();
        c.setName("test course");
        c.setId(UUID.fromString("473a1ec4-a336-4592-a62f-290e828b4148"));
        c.setStartDate(LocalDate.parse("2025-05-11"));
        c.setEndDate(LocalDate.parse("2025-08-11"));
        c.setStatus(1);
        given(courseRepository.existsByName(c.getName())).willThrow(new ResourceAlreadyExistException(c.getName(),"Name"));



        Throwable thrown=assertThrows(ResourceAlreadyExistException.class, ()->{
            courseService.createCourse(c);
        });
        assertThat(thrown.getMessage()).isEqualTo("Name was already exist with test course");
        verify(courseRepository, times(1)).existsByName(c.getName());

    }
}
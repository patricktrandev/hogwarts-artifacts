package com.cloudinfo.hogwartsartifact.course;

import com.cloudinfo.hogwartsartifact.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAllWizards() {
    }

    @Test
    void findWizard() {
    }

    @Test
    void givenCourse_whenCreateCourse_thenReturn200Success() throws Exception {
        Course c= new Course();
        c.setName("test course");
        c.setId("3278463782");
        c.setStartDate(LocalDate.parse("2025-05-11"));
        c.setEndDate(LocalDate.parse("2025-08-11"));
        c.setStatus(1);
        c.setWizards(new HashSet<>());
        given(courseService.createCourse(any(Course.class))).willReturn(c);
        ResultActions response= mockMvc.perform(post("/api/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(c)));
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Create course successfully"))
                .andExpect(jsonPath("$.data.name").value(c.getName()));
    }
    @Test
    void givenCourse_whenFindById_thenReturn200Success() throws Exception {
        Course c= new Course();
        c.setName("test course");
        c.setId("3278463782");
        c.setStartDate(LocalDate.parse("2025-05-11"));
        c.setEndDate(LocalDate.parse("2025-08-11"));
        c.setStatus(1);
        c.setWizards(new HashSet<>());
        given(courseService.findCourseById(c.getId())).willReturn(c);
        ResultActions response= mockMvc.perform(get("/api/v1/courses/{id}",c.getId()));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Find one course successfully"))
                .andExpect(jsonPath("$.data.name").value(c.getName()));
    }
    @Test
    void givenCourse_whenFindById_thenReturn400() throws Exception {
        Course c= new Course();
        c.setName("test course");
        c.setId("3278463782");
        c.setStartDate(LocalDate.parse("2025-05-11"));
        c.setEndDate(LocalDate.parse("2025-08-11"));
        c.setStatus(1);
        c.setWizards(new HashSet<>());
        given(courseService.findCourseById(c.getId())).willThrow(new ResourceNotFoundException(c.getId()));
        ResultActions response= mockMvc.perform(get("/api/v1/courses/{id}",c.getId()));
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Could not found with Id 3278463782"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void givenCourse_whenFindAllCourses_thenReturn200Success() throws Exception {
        List<Course> courseList= new ArrayList<>();
        Course c= new Course();
        c.setName("test course");
        c.setId("3278463782");
        c.setStartDate(LocalDate.parse("2025-05-11"));
        c.setEndDate(LocalDate.parse("2025-08-11"));
        c.setStatus(1);
        c.setWizards(new HashSet<>());
        Course c1= new Course();
        c1.setName("test course");
        c1.setId("3278463782");
        c1.setStartDate(LocalDate.parse("2025-05-11"));
        c1.setEndDate(LocalDate.parse("2025-08-11"));
        c1.setStatus(1);
        c1.setWizards(new HashSet<>());
        courseList.add(c);
        courseList.add(c1);
        given(courseService.findAllCourses()).willReturn(courseList);
        ResultActions response= mockMvc.perform(get("/api/v1/courses"));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Find all courses successfully"))
                .andExpect(jsonPath("$.data.size()").value(courseList.size()));
    }
    @Test
    void givenCourse_whenFindAllCourses_thenReturnEmptyList() throws Exception {

        given(courseService.findAllCourses()).willReturn(List.of());
        ResultActions response= mockMvc.perform(get("/api/v1/courses"));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Find all courses successfully"))
                .andExpect(jsonPath("$.data.size()").value(0));
    }
}
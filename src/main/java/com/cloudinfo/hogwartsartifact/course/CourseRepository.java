package com.cloudinfo.hogwartsartifact.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {
   Boolean existsByName(String name);
}

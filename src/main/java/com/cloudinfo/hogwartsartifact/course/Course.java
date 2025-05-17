package com.cloudinfo.hogwartsartifact.course;

import com.cloudinfo.hogwartsartifact.wizard.Wizard;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;

    @ManyToMany(mappedBy = "courses")
    private Set<Wizard> wizards= new HashSet<>();



}

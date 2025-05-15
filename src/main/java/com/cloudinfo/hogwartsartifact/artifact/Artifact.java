package com.cloudinfo.hogwartsartifact.artifact;

import com.cloudinfo.hogwartsartifact.wizard.Wizard;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

public class Artifact {
    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;
    private String imageUrl;

    @ManyToOne
    private Wizard owner;

}

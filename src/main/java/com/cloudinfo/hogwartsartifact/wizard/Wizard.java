package com.cloudinfo.hogwartsartifact.wizard;

import com.cloudinfo.hogwartsartifact.artifact.Artifact;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Wizard implements Serializable {
    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Artifact> artifacts= new ArrayList<>();

    public void addArtifatc(Artifact artifact){
        artifact.setOwner(this);
        this.artifacts.add(artifact);
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }
}

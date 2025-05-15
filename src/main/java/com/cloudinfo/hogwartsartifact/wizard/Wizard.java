package com.cloudinfo.hogwartsartifact.wizard;

import com.cloudinfo.hogwartsartifact.artifact.Artifact;
import com.cloudinfo.hogwartsartifact.course.Course;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Wizard implements Serializable {
    @Id
    private String id;
    private String name;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Artifact> artifacts= new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "wizard_course",
            joinColumns = @JoinColumn(name = "wizard_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses= new HashSet<>();


    public void addCourse(Course course){
        course.getWizards().add(this);
        this.courses.add(course);

    }

    public void removeCourse(Course course){
        this.courses.remove(course);
        course.getWizards().remove(this);

    }

    public void addArtifatc(Artifact artifact){

        this.artifacts.add(artifact);
        artifact.setOwner(this);//chieu nguoc lai
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }

    public void removeAllArtifacts() {
        artifacts.forEach(item-> item.setOwner(null));
        this.artifacts.clear();
    }
    public void removeArtifact(Artifact artifact){
        artifact.setOwner(null);
        artifacts.remove(artifact);
    }
}

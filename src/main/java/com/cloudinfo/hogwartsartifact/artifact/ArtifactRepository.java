package com.cloudinfo.hogwartsartifact.artifact;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactRepository extends JpaRepository<Artifact, String> {
}

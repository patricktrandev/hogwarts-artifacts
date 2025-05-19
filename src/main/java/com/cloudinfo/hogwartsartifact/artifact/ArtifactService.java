package com.cloudinfo.hogwartsartifact.artifact;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArtifactService {
    Artifact findById(String artifactId);
    List<Artifact> findAllArtifacts();
    Artifact saveArtifact(Artifact artifact);


    Artifact updateArtifact(String id, Artifact artifactDto);

    void deleteArtifact(String id);
}

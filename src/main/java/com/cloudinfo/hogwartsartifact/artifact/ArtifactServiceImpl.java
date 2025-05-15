package com.cloudinfo.hogwartsartifact.artifact;

import com.cloudinfo.hogwartsartifact.artifact.dto.ArtifactDto;
import com.cloudinfo.hogwartsartifact.exception.ArtifactNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtifactServiceImpl implements ArtifactService{

    private final ArtifactRepository artifactRepository;




    @Override
    public Artifact findById(String artifactId){
        Artifact result=artifactRepository.findById(artifactId).orElseThrow(()-> new ArtifactNotFoundException(artifactId));

        return result;
    }

    @Override
    public List<Artifact> findAllArtifacts() {
        List<Artifact> artifactList=artifactRepository.findAll();
        return artifactList;
    }

    @Override
    public Artifact saveArtifact(Artifact artifact) {

        UUID uuid = UUID.randomUUID();
        artifact.setId(uuid+"");

        Artifact saved=artifactRepository.save(artifact);
        return saved;
    }

    @Override
    public Artifact updateArtifact(String id, Artifact artifactDto) {
        return artifactRepository.findById(id)
                .map((item)-> {
                    item.setName(artifactDto.getName());
                    item.setDescription(artifactDto.getDescription());
                    item.setImageUrl(artifactDto.getImageUrl());
                    return artifactRepository.save(item);
                }).orElseThrow(()-> new ArtifactNotFoundException(id));

    }

    @Override
    public void deleteArtifact(String id) {
        Artifact result=artifactRepository.findById(id).orElseThrow(()-> new ArtifactNotFoundException(id));
        artifactRepository.deleteById(result.getId());
    }


}

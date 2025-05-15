package com.cloudinfo.hogwartsartifact.artifact.converter;

import com.cloudinfo.hogwartsartifact.artifact.Artifact;
import com.cloudinfo.hogwartsartifact.artifact.dto.ArtifactDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoMapper implements Converter<ArtifactDto,Artifact > {
    @Override
    public Artifact convert(ArtifactDto source) {
        Artifact artifact= new Artifact();
        artifact.setId(source.id());
        artifact.setName(source.name());
        artifact.setDescription(source.description());
        artifact.setImageUrl(source.imageUrl());

        return artifact;
    }
}

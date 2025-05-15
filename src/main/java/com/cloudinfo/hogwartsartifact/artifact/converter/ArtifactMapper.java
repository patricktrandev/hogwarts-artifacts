package com.cloudinfo.hogwartsartifact.artifact.converter;

import com.cloudinfo.hogwartsartifact.artifact.Artifact;
import com.cloudinfo.hogwartsartifact.artifact.dto.ArtifactDto;

import com.cloudinfo.hogwartsartifact.wizard.converter.WizardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtifactMapper implements Converter<Artifact,ArtifactDto> {
    private final WizardMapper wizardMapper;

    @Override
    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto= new ArtifactDto(source.getId(), source.getName(),source.getDescription(),source.getImageUrl(),
                source.getOwner()!=null?wizardMapper.convert(source.getOwner()):null);
        return artifactDto;
    }
}

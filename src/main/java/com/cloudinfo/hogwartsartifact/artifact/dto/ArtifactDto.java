package com.cloudinfo.hogwartsartifact.artifact.dto;

import com.cloudinfo.hogwartsartifact.wizard.dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(String id,
                          @NotEmpty(message = "Name is required.")
                          String name,
                          String description, String imageUrl, WizardDto owner) {

}

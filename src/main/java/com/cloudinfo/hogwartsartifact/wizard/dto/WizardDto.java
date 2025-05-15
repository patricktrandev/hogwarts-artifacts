package com.cloudinfo.hogwartsartifact.wizard.dto;

import jakarta.validation.constraints.NotEmpty;

public record WizardDto(String id,
                        @NotEmpty(message = "Wizard name is required")
                        String name, Integer numberOfArtifacts) {
}

package com.cloudinfo.hogwartsartifact.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.Set;

public record AccountDto(
        String id,
        @NotEmpty(message = "Username is required")
        String username,
        @NotEmpty(message = "Email is required")
        @Email(message = "Please use a correct format of email")
        String email,
        String type,
        boolean enabled,
        int numberOfRoles,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        @JsonProperty("created_by")
        String createdBy,
        @JsonProperty("updated_at")
        LocalDateTime updatedAt,
        @JsonProperty("updated_by")
        String updatedBy
) {
}

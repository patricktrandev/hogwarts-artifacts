package com.cloudinfo.hogwartsartifact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditAwareImpl {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("test-auditor");
    }
}

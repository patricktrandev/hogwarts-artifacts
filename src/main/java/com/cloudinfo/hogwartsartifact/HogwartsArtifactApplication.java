package com.cloudinfo.hogwartsartifact;

import com.cloudinfo.hogwartsartifact.artifact.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class HogwartsArtifactApplication {

	public static void main(String[] args) {
		SpringApplication.run(HogwartsArtifactApplication.class, args);
	}



}

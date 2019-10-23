package com.semafoor.semaforce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Configuration class that enables Jpa auditing and provides the information needed for the CreatedBy and ModifiedBy
 * properties of the com.semafoor.semaforce.model.entities.AbstractEntity class.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {

        return () -> Optional.of("toBeDefined");
    }
}

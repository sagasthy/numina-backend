package com.kar.numina.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    @Bean
    public FlywayMigrationStrategy repairAndMigrateStrategy() {
        return flyway -> {
            flyway.repair(); // Repair the schema history table
            flyway.migrate(); // Then perform migrations
        };
    }
}


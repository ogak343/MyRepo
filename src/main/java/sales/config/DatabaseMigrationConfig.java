package sales.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseMigrationConfig {

    private final Flyway flyway;

    @Autowired
    public DatabaseMigrationConfig(Flyway flyway) {
        this.flyway = flyway;
    }

    @Bean
    public ApplicationRunner flywayMigration() {
        return args -> flyway.migrate();
    }
}

package com.example.notificaciones_service.config;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase l = new SpringLiquibase();
        l.setDataSource(dataSource);
        l.setChangeLog("classpath:db/changelog/db.changelog-001-notificacion.sql");
        l.setShouldRun(true);
        return l;
    }
}

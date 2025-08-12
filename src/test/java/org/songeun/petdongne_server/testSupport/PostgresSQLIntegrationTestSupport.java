package org.songeun.petdongne_server.testSupport;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresSQLIntegrationTestSupport extends IntegrationTestSupport {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("init-postgresql.sql"); // todo exetension 추가, 공간 & pg_trgm?

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.service.jdbc-url", postgres::getJdbcUrl);
        registry.add("spring.datasource.service.username", postgres::getUsername);
        registry.add("spring.datasource.service.password", postgres::getPassword);
        registry.add("spring.datasource.service.driver-class-name", () -> "org.postgresql.Driver");
    }

}

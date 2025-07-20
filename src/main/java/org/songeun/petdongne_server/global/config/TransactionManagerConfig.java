package org.songeun.petdongne_server.global.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class TransactionManagerConfig {

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // 스프링 배치 메타 데이터 관리용 트랜잭션 매니저
    @Bean
    @BatchTransactionManager
    public PlatformTransactionManager batchTransactionManager(@BatchDataSource DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

}

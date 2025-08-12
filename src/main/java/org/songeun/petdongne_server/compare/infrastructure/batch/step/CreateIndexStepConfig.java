package org.songeun.petdongne_server.compare.infrastructure.batch.step;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.infrastructure.schema.AddressSchemaManager;
import org.songeun.petdongne_server.global.util.HashGenerator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static org.songeun.petdongne_server.compare.domain.AddressTableMetaData.*;
import static org.songeun.petdongne_server.compare.domain.AddressTableMetaData.addressInitialsColumnName;

@Configuration
@RequiredArgsConstructor
public class CreateIndexStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AddressSchemaManager schemaManager;

    @Bean
    @Qualifier("createIndexStep")
    public Step createIndex() {
        return new StepBuilder("createIndexStep", jobRepository)
                .tasklet(createGinIndexTasklet(null), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet createGinIndexTasklet(
            @Value("#{jobParameters['newTableName']}") String tableName
    ) {
        // 스키마 책임 이관
        String indexPrefix = HashGenerator.generate(tableName).substring(0, 6);
        String fullAddressIdxName = "a"+indexPrefix + "_" + getFullAddressGinIndexName();
        String initialsIdxName = "a"+indexPrefix + "_" + getAddressInitialsGinIndexName();

        return (contribution, chunkContext) -> {
            schemaManager.createGinExtensionIfNotExist();
            schemaManager.createGinIndex(tableName, fullAddressIdxName, fullAddressColumnName());
            schemaManager.createGinIndex(tableName, initialsIdxName, addressInitialsColumnName());
            return RepeatStatus.FINISHED;
        };
    }

}

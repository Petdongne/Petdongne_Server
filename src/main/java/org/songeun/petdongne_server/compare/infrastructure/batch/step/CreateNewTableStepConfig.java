package org.songeun.petdongne_server.compare.infrastructure.batch.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.compare.infrastructure.batch.exception.AddressBatchErrorStatus;
import org.songeun.petdongne_server.compare.infrastructure.batch.exception.NewAddressBatchException;
import org.songeun.petdongne_server.compare.infrastructure.schema.AddressSchemaManager;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CreateNewTableStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AddressSchemaManager schemaManager;

    @Bean
    @Qualifier("createNewAddressTableStep")
    public Step createNewAddressTable(
    ) {
        return new StepBuilder("createNewAddressTableStep", jobRepository)
                .tasklet(createNewAddressTasklet(null), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet createNewAddressTasklet(
            @Value("#{jobParameters['newTableName']}") String tableName
    ) {
        return ((contribution, chunkContext) -> {

            if (schemaManager.existTable(tableName)) {
                log.error("테이블 이름 {} 중복 발생", tableName);
                throw new NewAddressBatchException(AddressBatchErrorStatus.TABLE_NAME_ALREADY_EXISTS);
            }

            schemaManager.createTableIfNotExist(tableName);
            contribution.getStepExecution().getExecutionContext().put("newTableName", tableName);

            return RepeatStatus.FINISHED;
        });
    }

}

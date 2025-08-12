package org.songeun.petdongne_server.compare.infrastructure.batch.step;

import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
public class CreateOrReplaceViewStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AddressSchemaManager schemaManager;

    @Bean
    @Qualifier("createOrReplaceViewStep")
    public Step createOrReplaceViewStep() {
        return new StepBuilder("createOrReplaceViewStep", jobRepository)
                .tasklet(createOrReplaceViewTasklet(null), transactionManager).build();
    }


    @Bean
    @StepScope
    public Tasklet createOrReplaceViewTasklet(
            @Value("#{jobParameters['newTableName']}") String tableName
    ) {
        return (((contribution, chunkContext) -> {
            schemaManager.createOrReplaceView(tableName);
            return RepeatStatus.FINISHED;
        }));
    }

}

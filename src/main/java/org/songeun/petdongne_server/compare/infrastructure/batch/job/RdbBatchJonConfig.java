package org.songeun.petdongne_server.compare.infrastructure.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class RdbBatchJonConfig {

    private final JobRepository jobRepository;

    private final Step createNewAddressTableStep;
    private final Step savingLegalDongAddressStep;
    private final Step upsertAdminDongAddressStep;
    private final Step createIndexStep;
    private final Step createOrReplaceViewStep;

    public RdbBatchJonConfig(
            JobRepository jobRepository,
            @Qualifier("createNewAddressTableStep") Step createNewAddressTableStep,
            @Qualifier("savingLegalDongAddressStep") Step savingLegalDongAddressStep,
            @Qualifier("upsertAdminDongAddressStep") Step upsertAdminDongAddressStep,
            @Qualifier("createIndexStep") Step createIndexStep,
            @Qualifier("createOrReplaceViewStep") Step createOrReplaceViewStep
    ) {
        this.jobRepository = jobRepository;
        this.createNewAddressTableStep = createNewAddressTableStep;
        this.savingLegalDongAddressStep = savingLegalDongAddressStep;
        this.upsertAdminDongAddressStep = upsertAdminDongAddressStep;
        this.createIndexStep = createIndexStep;
        this.createOrReplaceViewStep = createOrReplaceViewStep;
    }

    @Bean
    public Job updateAddressJob() {
        return new JobBuilder("updateAddressJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(createNewAddressTableStep)
                .next(savingLegalDongAddressStep)
                .next(upsertAdminDongAddressStep)
                .next(createIndexStep)
                .next(createOrReplaceViewStep)
                .build();
    }

}

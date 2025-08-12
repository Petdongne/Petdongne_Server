package org.songeun.petdongne_server.address.infrastructure.batch.config;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.batch.adminDong.AdminDongAddressRow;
import org.songeun.petdongne_server.address.infrastructure.batch.index.CreateNewAddressIndexTasklet;
import org.songeun.petdongne_server.address.infrastructure.batch.index.IndexingErrorListener;
import org.songeun.petdongne_server.address.infrastructure.batch.index.SwitchAddressAliasTargetTasklet;
import org.songeun.petdongne_server.address.infrastructure.batch.legalDong.LegalDongAddressRow;
import org.songeun.petdongne_server.address.infrastructure.batch.adminDong.AdminDongAddressItemProcessor;
import org.songeun.petdongne_server.address.infrastructure.batch.adminDong.AdminDongAddressItemWriter;
import org.songeun.petdongne_server.address.infrastructure.batch.legalDong.LegalDongAddressItemProcessor;
import org.songeun.petdongne_server.address.infrastructure.batch.legalDong.LegalDongAddressItemWriter;
import org.songeun.petdongne_server.address.infrastructure.batch.adminDong.AdminDongAddressRowMapper;
import org.songeun.petdongne_server.address.infrastructure.batch.legalDong.LegalDongAddressRowMapper;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
public class AddressIndexingJobConfig {

/*    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    // 인덱스 관련
    private final CreateNewAddressIndexTasklet createNewAddressIndexTasklet;
    private final SwitchAddressAliasTargetTasklet switchAddressAliasTargetTasklet;
    private final IndexingErrorListener indexingErrorListener;

    // 법정동 주소 관련
    private final LegalDongAddressItemProcessor legalDongAddressItemProcessor;
    private final LegalDongAddressItemWriter legalDongAddressItemWriter;
    private final LegalDongAddressRowMapper legalDongAddressRowMapper;

    // 행정동 주소 관련
    private final AdminDongAddressItemProcessor adminDongAddressItemProcessor;
    private final AdminDongAddressItemWriter adminDongAddressItemWriter;
    private final AdminDongAddressRowMapper adminDongAddressRowMapper;

    @Bean
    public Job addressIndexingJob() {
        return new JobBuilder("addressIndexingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(createNewAddressIndexStep())
                .next(indexingAdminDongAddressStep())
                .next(indexingLegalDongAddressStep())
                .next(switchAliasTargetStep())
                .build();
    }

    @Bean
    public Step switchAliasTargetStep() {
        return new StepBuilder("switchAliasTargetStep", jobRepository)
                .tasklet(switchAddressAliasTargetTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step indexingLegalDongAddressStep() {

        return new StepBuilder("indexingLegalDongAddressStep", jobRepository)
                .<LegalDongAddressRow, AddressDocument>chunk(100, transactionManager)
                .reader(legalDongAddressReader(null))
                .processor(legalDongAddressItemProcessor)
                .writer(legalDongAddressItemWriter)
                .listener(indexingErrorListener)
                .build();
    }

    @Bean
    public Step indexingAdminDongAddressStep() {
        return new StepBuilder("indexingAdminDongAddressStep", jobRepository)
                .<AdminDongAddressRow, AddressDocument>chunk(100, transactionManager)
                .reader(adminDongAddressReader(null))
                .processor(adminDongAddressItemProcessor)
                .writer(adminDongAddressItemWriter)
                .listener(indexingErrorListener)
                .build();
    }

    @Bean
    public Step createNewAddressIndexStep() {
        return new StepBuilder("createNewAddressIndexStep", jobRepository)
                .tasklet(createNewAddressIndexTasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public PoiItemReader<AdminDongAddressRow> adminDongAddressReader(
            @Value("#{jobParameters['adminDongAddressFilePath']}") String filePath
    ){
        PoiItemReader<AdminDongAddressRow> poiItemReader = new PoiItemReader<>();
        poiItemReader.setResource(new FileSystemResource(Path.of(filePath)));
        poiItemReader.setLinesToSkip(1);
        poiItemReader.setRowMapper(adminDongAddressRowMapper);

        return poiItemReader;
    }

    @Bean
    @StepScope
    public PoiItemReader<LegalDongAddressRow> legalDongAddressReader(
            @Value("#{jobParameters['legalDongAddressFilePath']}") String filePath
    ){
        PoiItemReader<LegalDongAddressRow> poiItemReader = new PoiItemReader<>();
        poiItemReader.setResource(new FileSystemResource(Path.of(filePath)));
        poiItemReader.setLinesToSkip(1);
        poiItemReader.setRowMapper(legalDongAddressRowMapper);

        return poiItemReader;
    }*/

}

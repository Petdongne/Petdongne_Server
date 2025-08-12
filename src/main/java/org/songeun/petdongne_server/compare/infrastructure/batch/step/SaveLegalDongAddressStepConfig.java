package org.songeun.petdongne_server.compare.infrastructure.batch.step;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.batch.legalDong.LegalDongAddressRow;
import org.songeun.petdongne_server.address.infrastructure.batch.legalDong.LegalDongAddressRowMapper;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.LegalDongAddressParts;
import org.songeun.petdongne_server.compare.domain.Address;
import org.songeun.petdongne_server.compare.infrastructure.sql.AddressDmlSqlGenerator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class SaveLegalDongAddressStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final LegalDongAddressRowMapper legalDongAddressRowMapper;
    private final AddressDmlSqlGenerator dmlSqlGenerator;
    private final DataSource dataSource;

    private static final String codeParamName = "code";
    private static final String fullAddressParamName = "fullAddress";
    private static final String addressInitialsParamName = "addressInitials";
    private static final String addressTypeParamName = "addressType";

    @Bean
    @Qualifier("savingLegalDongAddressStep")
    public Step savingLegalDongAddressStep() {
        return new StepBuilder("savingLegalDongAddressStep", jobRepository)
                .<LegalDongAddressRow, Address>chunk(100, transactionManager)
                .reader(legalDongAddressFileReader(null))
                .processor(legalDongAddressProcessor())
                .writer(legalDongAddressWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public PoiItemReader<LegalDongAddressRow> legalDongAddressFileReader(
            @Value("#{jobParameters['legalDongAddressFilePath']}") String filePath
    ){
        PoiItemReader<LegalDongAddressRow> poiItemReader = new PoiItemReader<>();
        poiItemReader.setResource(new FileSystemResource(filePath));
        poiItemReader.setLinesToSkip(1);
        poiItemReader.setRowMapper(legalDongAddressRowMapper);

        return poiItemReader;
    }

    @Bean
    public ItemProcessor<LegalDongAddressRow, Address> legalDongAddressProcessor() {
        return (item -> {
            // 말소된 데이터는 처리하지 않음
            if (!item.isExpired(LocalDate.now())) {
                return null;
            }

            String code = item.getCode();
            String sido = item.getSido();
            String sigungu = item.getSigungu();
            String eupmyeondong = item.getEupmyeondong();
            String re = item.getRe();
            LegalDongAddressParts addressParts = LegalDongAddressParts.create(sido, sigungu, eupmyeondong, re);

            return Address.create(code, addressParts);
        });
    }

    @Bean
    @StepScope
    public ItemWriter<Address> legalDongAddressWriter(
            @Value("#{jobParameters['newTableName']}") String tableName
    ) {
        String sql = dmlSqlGenerator.getDoNothingUpsertSqlTemplate(tableName,
                codeParamName, fullAddressParamName, addressInitialsParamName, addressTypeParamName);

        return new JdbcBatchItemWriterBuilder<Address>()
                .dataSource(dataSource)
                .sql(sql)
                .assertUpdates(false)
                .itemSqlParameterSourceProvider(item -> {
                    MapSqlParameterSource paramSource = new MapSqlParameterSource();
                    paramSource.addValue(codeParamName, item.getCode());
                    paramSource.addValue(fullAddressParamName, item.getFullAddress());
                    paramSource.addValue(addressInitialsParamName, item.getAddressInitials());
                    paramSource.addValue(addressTypeParamName, item.getType().getKoreanName());
                    return paramSource;
                }).build();
    }

}

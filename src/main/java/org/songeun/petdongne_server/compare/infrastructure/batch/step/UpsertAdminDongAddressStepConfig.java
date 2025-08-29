package org.songeun.petdongne_server.compare.infrastructure.batch.step;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.batch.adminDong.AdminDongAddressRow;
import org.songeun.petdongne_server.address.infrastructure.batch.adminDong.AdminDongAddressRowMapper;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AdminDongAddressParts;
import org.songeun.petdongne_server.compare.domain.entity.Address;
import org.songeun.petdongne_server.compare.infrastructure.sql.AddressDmlSqlGenerator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
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
public class UpsertAdminDongAddressStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final AddressDmlSqlGenerator dmlSqlGenerator;
    private final AdminDongAddressRowMapper adminDongAddressRowMapper;
    private final DataSource dataSource;

    private static final String codeParamName = "code";
    private static final String fullAddressParamName = "fullAddress";
    private static final String addressInitialsParamName = "addressInitials";
    private static final String addressTypeParamName = "addressType";

    @Bean
    @Qualifier("upsertAdminDongAddressStep")
    public Step upsertAdminDongAddressStep() {
        return new StepBuilder("upsertAdminDongAddressStep", jobRepository)
                .<AdminDongAddressRow, Address>chunk(100, transactionManager)
                .reader(adminDongAddressFileReader(null))
                .processor(adminDongAddressProcessor())
                .writer(adminDongAddressWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public PoiItemReader<AdminDongAddressRow> adminDongAddressFileReader(
            @Value("#{jobParameters['adminDongAddressFilePath']}") String filePath
    ){
        PoiItemReader<AdminDongAddressRow> poiItemReader = new PoiItemReader<>();
        poiItemReader.setResource(new FileSystemResource(filePath));
        poiItemReader.setLinesToSkip(1);
        poiItemReader.setRowMapper(adminDongAddressRowMapper);

        return poiItemReader;
    }

    @Bean
    public ItemProcessor<AdminDongAddressRow, Address> adminDongAddressProcessor() {
        return (item -> {
            // 말소된 데이터는 처리하지 않음
            if (!item.isValid(LocalDate.now())) {
                return null;
            }

            String code = item.getCode();
            String sido = item.getSido();
            String sigungu = item.getSigungu();
            String eupmyeondong = item.getEupmyeondong();
            AdminDongAddressParts addressParts = AdminDongAddressParts.create(sido, sigungu, eupmyeondong);

            return Address.create(code, addressParts);
        });
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Address> adminDongAddressWriter(
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

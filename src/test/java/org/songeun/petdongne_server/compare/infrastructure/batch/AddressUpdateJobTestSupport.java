package org.songeun.petdongne_server.compare.infrastructure.batch;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.songeun.petdongne_server.compare.domain.entity.AddressTableMetaData;
import org.songeun.petdongne_server.compare.infrastructure.schema.AddressSchemaManager;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.batch.core.*;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public abstract class AddressUpdateJobTestSupport extends IntegrationTestSupport {

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    protected Job updateAddressJob;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected AddressSchemaManager schemaManager;

    @AfterEach
    void tearDown() {
        String dropSql = String.format("DROP TABLE IF EXISTS %s CASCADE", testAddressTableName());
        jdbcTemplate.execute(dropSql);
    }

    @PostConstruct
    public void configureJobLauncherTestUtils() throws Exception {
        jobLauncherTestUtils.setJob(updateAddressJob);
    }

    protected String testAddressTableName() {
        return "address_test";
    }

    protected String createAddressTable(String tableName) {
        schemaManager.createTableIfNotExist(tableName);

        return tableName;
    }

    protected <T> List<T> executeItemReader(JobParameters jobParameters, PoiItemReader<T> itemReader) throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);

        return StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<T> list = new ArrayList<>();
            itemReader.open(new ExecutionContext());
            T item;

            while ((item = itemReader.read()) != null) {
                list.add(item);
            }

            itemReader.close();
            return list;
        });
    }

    protected void assertViewTargetsTable(String table) {
        String getViewTargetTableNameQuery = """
                SELECT substring(definition FROM 'FROM\\s+([a-zA-Z0-9_]+)') AS referenced_table
                FROM pg_views
                WHERE viewname = ?
                """;
        String viewTargetTable = jdbcTemplate.queryForObject(getViewTargetTableNameQuery, String.class, AddressTableMetaData.viewName());
        assertThat(viewTargetTable).isEqualTo(table);
    }

}

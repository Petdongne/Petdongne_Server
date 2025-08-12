package org.songeun.petdongne_server.address.infrastructure.batch;

import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 1. 배치
 * 테스트 -> 개발
 *
 * 2. 검색
 * 테스트 -> 개발
 */
@SpringBootTest
@SpringBatchTest
//public class NewAddressIndexingJobTest extends PostgresSQLIntegrationTestSupport {
public class NewAddressIndexingJobTest extends IntegrationTestSupport {

/*    @Autowired
    private Job addressSavingJob;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AddressDqlSqlGenerator dqlSqlGenerator;

    @Autowired
    private AddressSchemaManager schemaManager;

    @Autowired
    private PoiItemReader<AdminDongAddressRow> adminDongAddressFileReader;

    @Autowired
    private ItemProcessor<AdminDongAddressRow, Address> adminDongAddressProcessor;


    @Autowired
    private DeletedDataPolicy legacyDataNotUsedPolicy;

    @Autowired
    private AddressIdGenerator addressIdGenerator;

    @TempDir
    private Path tempDir;

    @AfterEach
    void tearDown() {
        String dropSql = String.format("DROP TABLE IF EXISTS %s", getTestAddressTableName());
        jdbcTemplate.execute(dropSql);
    }

    @PostConstruct
    public void configureJobLauncherTestUtils() throws Exception {
        jobLauncherTestUtils.setJob(addressSavingJob);
    }*/

}

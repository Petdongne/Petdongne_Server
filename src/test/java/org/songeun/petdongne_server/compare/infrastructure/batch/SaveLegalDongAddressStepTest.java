package org.songeun.petdongne_server.compare.infrastructure.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.songeun.petdongne_server.compare.infrastructure.batch.file.LegalDongAddressRow;
import org.songeun.petdongne_server.compare.domain.LegalDongAddressParts;
import org.songeun.petdongne_server.compare.domain.entity.Address;
import org.songeun.petdongne_server.compare.fixture.AddressFileFixtureFactory;
import org.songeun.petdongne_server.global.batch.policy.DeletedDataPolicy;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.songeun.petdongne_server.compare.domain.entity.AddressTableMetaData.*;

class SaveLegalDongAddressStepTest extends AddressUpdateJobTestSupport {

    @TempDir
    private Path tempDir;

    @Autowired
    private PoiItemReader<LegalDongAddressRow> legalDongAddressFileReader;

    @Autowired
    private ItemProcessor<LegalDongAddressRow, Address> legalDongAddressProcessor;

    @Autowired
    private JdbcBatchItemWriter<Address> legalDongAddressWriter;

    @Autowired
    private DeletedDataPolicy legacyDataNotUsedPolicy;

    @Test
    @DisplayName("법정동 주소 파일 내용을 객체로 매핑한다.")
    void shouldMappingLegalDongAddressFileToAddress() throws Exception {
        //given
        var legalDongFixture = AddressFileFixtureFactory.uniqueLegalDong();
        var legalDongFile = legalDongFixture.toExcelFile("legalDong", tempDir);

        var jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("legalDongAddressFilePath", legalDongFile.toString())
                .toJobParameters();

        //when
        List<LegalDongAddressRow> mappedRows = executeItemReader(jobParameters, legalDongAddressFileReader);

        //then
        List<List<String>> originRows = legalDongFixture.getRows();
        for (int i = 0; i < originRows.size(); i++) {
            assertRowEquals(originRows.get(i), mappedRows.get(i));
        }
    }

    private void assertRowEquals(List<String> expected, LegalDongAddressRow actual) {
        assertThat(actual.getCode()).isEqualTo(expected.get(0));
        assertThat(actual.getSido()).isEqualTo(expected.get(1));
        assertThat(actual.getSigungu()).isEqualTo(Objects.equals(expected.get(2), "") ? null : expected.get(2));
        assertThat(actual.getEupmyeondong()).isEqualTo(Objects.equals(expected.get(3), "") ? null : expected.get(3));
        assertThat(actual.getRe()).isEqualTo(Objects.equals(expected.get(4), "") ? null : expected.get(4));
    }

    @Test
    @DisplayName("법정동 파일 매핑 객체를 도메인 주소 객체로 변환한다.")
    void shouldConvertLegalDongAddressToDomainObj() throws Exception {
        //given
        var creationDate = LocalDate.now();
        var addressParts = LegalDongAddressParts.create("전라북도", "해산군", "글포동", "마포리");
        var legalDongRow = createLegalDongRow(addressParts, creationDate, null);

        //when
        Address address = legalDongAddressProcessor.process(legalDongRow);

        //then
        assertThat(address.getAddressInitials()).isEqualTo(addressParts.toAddressInitials());
        assertThat(address.getType()).isEqualTo(addressParts.toAddressType());

        String fullAddress = addressParts.toFullAddress();
        assertThat(address.getFullAddress()).isEqualTo(fullAddress);
    }

    @Test
    @DisplayName("유효하지 않은 법정동 파일 매핑 객체는 처리하지 않는다.")
    void shouldNotConvertDeletedLegalDongAddress() throws Exception {
        //given
        var creationDate = LocalDate.of(2020, 7, 10);
        var deletedDate = LocalDate.now().minusDays(1);
        var addressParts = LegalDongAddressParts.create("전라북도", "해산군", "글포동", "마포리");
        var legalDongRow = createLegalDongRow(addressParts, creationDate, deletedDate);

        //when
        Address processed = legalDongAddressProcessor.process(legalDongRow);

        //then
        assertThat(processed).isNull();
    }

    @Test
    @DisplayName("주소 객체를 주소 테이블에 저장한다.")
    void shouldSaveLegalDongAddress() throws Exception {
        //given
        String createdTableName = createAddressTable(testAddressTableName());
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("newTableName", createdTableName)
                .toJobParameters();

        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);
        List<Address> toWriteAddressees = AddressFileFixtureFactory.uniqueLegalDong().toAddresses();

        //when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            legalDongAddressWriter.write(new Chunk<>(toWriteAddressees));
            return null;
        });

        //then
        assertWriteDatabase(createdTableName, toWriteAddressees);
    }

    private void assertWriteDatabase(String tableName, List<Address> addresses) {
        String selectSql = String.format("SELECT * FROM %s", tableName);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(selectSql);

        // 개수 확인
        assertThat(results).hasSize(addresses.size());

        //첫 번째 주소만 확인
        Address expected = addresses.get(0);
        Map<String, Object> firstRow = results.get(0);
        assertThat(firstRow.get(fullAddressColumnName())).isEqualTo(expected.getFullAddress());
        assertThat(firstRow.get(addressInitialsColumnName())).isEqualTo(expected.getAddressInitials());
        assertThat(firstRow.get(addressTypeColumnName())).isEqualTo(expected.getType().getKoreanName());
    }

    private LegalDongAddressRow createLegalDongRow(LegalDongAddressParts addressParts, LocalDate creationDate, LocalDate deletedDate) {
        return LegalDongAddressRow.create("1111111111", addressParts.getSido(), addressParts.getSigungu(),
                addressParts.getEupmyeondong(), addressParts.getRe(), creationDate, deletedDate, legacyDataNotUsedPolicy);
    }

}
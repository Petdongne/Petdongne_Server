package org.songeun.petdongne_server.compare.infrastructure.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.songeun.petdongne_server.compare.infrastructure.batch.file.AdminDongAddressRow;
import org.songeun.petdongne_server.compare.domain.entity.AdminDongAddressParts;
import org.songeun.petdongne_server.compare.domain.entity.Address;
import org.songeun.petdongne_server.compare.fixture.AddressFileFixtureFactory;
import org.songeun.petdongne_server.compare.infrastructure.sql.AddressDmlSqlGenerator;
import org.songeun.petdongne_server.global.batch.policy.DeletedDataPolicy;
import org.songeun.petdongne_server.testSupport.FileUtils;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class UpsertAdminDongAddressStepTest extends AddressUpdateJobTestSupport{

    @TempDir
    private Path tempDir;

    @Autowired
    private PoiItemReader<AdminDongAddressRow> adminDongAddressFileReader;

    @Autowired
    private ItemProcessor<AdminDongAddressRow, Address> adminDongAddressProcessor;

    @Autowired
    private DeletedDataPolicy legacyDataNotUsedPolicy;

    @Autowired
    private AddressDmlSqlGenerator addressDmlSqlGenerator;

    @Autowired
    private JdbcBatchItemWriter<Address> adminDongAddressWriter;

    @Autowired
    private JdbcBatchItemWriter<Address> legalDongAddressWriter;

    @Test
    @DisplayName("행정동 주소 파일 내용을 객체로 매핑한다.")
    void shouldMappingAdminDongAddressFileToObjects() throws Exception {
        //given
        var adminDongFixture = AddressFileFixtureFactory.uniqueAdminDong();
        var rows = adminDongFixture.getRows();
        Path adminDongFile = FileUtils.createFile(rows, "adminDong", tempDir);

        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("adminDongAddressFilePath", adminDongFile.toString())
                .toJobParameters();

        //when
        List<AdminDongAddressRow> addressRows = executeItemReader(jobParameters, adminDongAddressFileReader);

        // then
        List<List<String>> dataRows = rows.subList(1, rows.size()); // 헤더 제외
        for (int i = 0; i < dataRows.size(); i++) {
            assertRowEquals(dataRows.get(i), addressRows.get(i));
        }
    }

    private void assertRowEquals(List<String> expected, AdminDongAddressRow actual) {
        assertThat(actual.getCode()).isEqualTo(expected.get(0));
        assertThat(actual.getSido()).isEqualTo(expected.get(1));
        assertThat(actual.getSigungu()).isEqualTo(Objects.equals(expected.get(2), "") ? null : expected.get(2));
        assertThat(actual.getEupmyeondong()).isEqualTo(Objects.equals(expected.get(3), "") ? null : expected.get(3));
    }

    @Test
    @DisplayName("행정동 파일 매핑 객체를 도메인 주소 객체로 변환한다.")
    void shouldConvertAdminDongAddressToDomainObj() throws Exception {
        //given
        var creationDate = LocalDate.now();
        var addressParts = AdminDongAddressParts.create("인천광역시", "남동구", "논현동");
        var adminDongRow = createAdminDongRow(addressParts, creationDate, null);

        //when
        Address address = adminDongAddressProcessor.process(adminDongRow);

        //then
        assertThat(address.getAddressInitials()).isEqualTo(addressParts.toAddressInitials());
        assertThat(address.getType()).isEqualTo(addressParts.toAddressType());

        String fullAddress = addressParts.toFullAddress();
        assertThat(address.getFullAddress()).isEqualTo(fullAddress);
    }

    @Test
    @DisplayName("현재 날짜 기준으로 삭제된 데이터는 처리하지 않는다.")
    void shouldNotConvertDeletedAdminDongAddress() throws Exception {
        //given
        var creationDate = LocalDate.of(2020, 7, 10);
        var deletedDate = LocalDate.now().minusDays(1);
        var addressParts = AdminDongAddressParts.create("인천광역시", "남동구", "논현동");
        var adminDongRow = createAdminDongRow(addressParts, creationDate, deletedDate);

        //when
        Address processed = adminDongAddressProcessor.process(adminDongRow);

        //then
        assertThat(processed).isNull();
    }

    @Test
    @DisplayName("중복된 주소를 가지고 있을 경우 저장하지 않는다.")
    void shouldNotSaveDuplicatedAddress() throws Exception {
        //given
        String createdTableName = createAddressTable(testAddressTableName());
        AddressFileFixtureFactory.AddressFixtures sameAddressData = AddressFileFixtureFactory.duplicated();

        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("newTableName", createdTableName)
                .toJobParameters();

        saveLegalDongAddresses(sameAddressData.getLegalAddresses(), jobParameters);
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);

        //when
        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            adminDongAddressWriter.write(new Chunk<>(sameAddressData.getAdminAddresses().toAddresses()));
            return null;
        });

        //then
        assertAdminAddressNotSaved(sameAddressData.getAdminAddresses(), createdTableName);
    }

    private void assertAdminAddressNotSaved(AddressFileFixtureFactory.AdminDongAddressFixture adminDongAddressFixture, String addressTable) {
        List<String> ids = adminDongAddressFixture.getIdList();
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));

        String sql = String.format(
                "SELECT COUNT(*) FROM %s WHERE full_address IN (%s)",
                addressTable,
                inSql
        );
        Integer count = jdbcTemplate.queryForObject(sql, ids.toArray(), Integer.class);

        assertThat(count).isEqualTo(0);
    }

    private void saveLegalDongAddresses(AddressFileFixtureFactory.LegalDongAddressFixture legalDongAddressFixture, JobParameters jobParameters) throws Exception {
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters);
        List<Address> toWriteAddressees = legalDongAddressFixture.toAddresses();

        StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            legalDongAddressWriter.write(new Chunk<>(toWriteAddressees));
            return null;
        });
    }

    private AdminDongAddressRow createAdminDongRow(AdminDongAddressParts addressParts, LocalDate creationDate, LocalDate deletedDate) {
        return AdminDongAddressRow.create("1111111111", addressParts.getSido(), addressParts.getSigungu(),
                addressParts.getEupmyeondong(), creationDate, deletedDate, legacyDataNotUsedPolicy);
    }

}
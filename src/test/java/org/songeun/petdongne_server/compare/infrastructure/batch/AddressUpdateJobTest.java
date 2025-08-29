package org.songeun.petdongne_server.compare.infrastructure.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.songeun.petdongne_server.compare.domain.entity.Address;
import org.songeun.petdongne_server.compare.domain.AddressTableNameCreator;
import org.songeun.petdongne_server.testSupport.FileUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType.ADMIN_DONG_ADDRESS;
import static org.songeun.petdongne_server.compare.fixture.AddressFileFixtureFactory.*;
import static org.songeun.petdongne_server.compare.fixture.AddressFileFixtureFactory.unique;

public class AddressUpdateJobTest extends AddressUpdateJobTestSupport {

    @Autowired
    private AddressTableNameCreator tableNameCreator;

    @TempDir
    private Path tempDir;

    // todo job 이름 변경: address sync schedule
    // todo 스키마 변경
    @Test
    @DisplayName("업데이트로 인해 수정되는 데이터를 기록한다.")
    void shouldSaveChangesWhenUpdate() throws Exception {
        //given
        AddressFixtures baseTestData = unique();
        Address beforeUpdateAdminAddress = Address.create(
                "4146153000", "경기도 용인시 처인구 유림동", "경용처유", ADMIN_DONG_ADDRESS, LocalDate.of(2020, 8, 10));
        Address afterUpdateAdminAddress = Address.create(
                "4146153500", "경기도 용인시 처인구 유림1동", "경용처유", ADMIN_DONG_ADDRESS, LocalDate.now().plusDays(3));
        Path adminDongFilePath = setupAdminDongTestData(baseTestData.getAdminAddresses(), beforeUpdateAdminAddress, afterUpdateAdminAddress);

        Address beforeUpdateLegalAddress = Address.create(
                "4777036038", "경상북도 영덕군 영해면 대리", "경용처유", ADMIN_DONG_ADDRESS, LocalDate.of(2020, 8, 10));
        Address afterUpdateLegalAddress = Address.create(
                "4777036039", "경상북도 영덕군 영해면 대동리", "경용처유", ADMIN_DONG_ADDRESS, LocalDate.now().plusDays(3));
        Path legalDongFilePath = setupLegalDongTestData(baseTestData.getLegalAddresses(), beforeUpdateLegalAddress, afterUpdateLegalAddress);

        String tempTableName = tableNameCreator.createNewTableName();
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("legalDongAddressFilePath", legalDongFilePath.toString())
                .addString("adminDongAddressFilePath", adminDongFilePath.toString())
                .addString("tempTableName", tempTableName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertSaveAdminChanges(beforeUpdateAdminAddress, afterUpdateAdminAddress);
    }

    private Path setupLegalDongTestData(LegalDongAddressFixture legalAddresses, Address beforeUpdateLegalAddress, Address afterUpdateLegalAddress) {

    }

    private void assertSaveAdminChanges(Address beforeUpdateAdminAddress, Address afterUpdateAdminAddress) {
        // address_changes_log 테이블에 저장되어 있는지 확인
    }

    private Path setupAdminDongTestData(AdminDongAddressFixture baseAddressFixture, Address beforeUpdate, Address afterUpdate) throws Exception {
        saveBeforeUpdatedAddress(baseAddressFixture, beforeUpdate);
        return getAfterUpdatedAddress(baseAddressFixture, afterUpdate);
    }

    private Path getAfterUpdatedAddress(AdminDongAddressFixture adminDongAddressFixture, Address afterUpdate) throws Exception {
        List<Address> addresses = Stream.concat(
                adminDongAddressFixture.toAddresses().stream(),
                Stream.of(afterUpdate)
        ).toList();

        return FileUtils.createFile(adminDongAddressFixture.getRows(), "adminDong", tempDir);
    }

    private void saveBeforeUpdatedAddress(AdminDongAddressFixture adminDongAddressFixture, Address beforeUpdate) {
        List<Address> addresses = Stream.concat(
                adminDongAddressFixture.toAddresses().stream(),
                Stream.of(beforeUpdate)
        ).toList();

        // repository 통해 save
    }

    @Test
    @DisplayName("폐지되는 주소를 기록한다.")
    void should(){
        //given

        //when

        //then

    }

    @Test
    @DisplayName("생성되는 주소를 기록한다.")
    void 놰(){
        //given

        //when

        //then

    }

    @Test
    @DisplayName("새로운 주소 테이블을 생성하고, 해당 테이블을 대상으로 뷰를 생성한다.")
    void shouldIndexedWhenSuccessfullyJobCompleted() throws Exception {
        //given
        AddressFixtures testData = unique();
        AdminDongAddressFixture adminDongAddressFixture = testData.getAdminAddresses();
        LegalDongAddressFixture legalDongAddressFixture = testData.getLegalAddresses();

        Path legalDongFilePath = FileUtils.createFile(legalDongAddressFixture.getRows(), "legalDong", tempDir);
        Path adminDongFilePath = FileUtils.createFile(adminDongAddressFixture.getRows(), "adminDong", tempDir);

        String newTableName = tableNameCreator.createNewTableName();
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("legalDongAddressFilePath", legalDongFilePath.toString())
                .addString("adminDongAddressFilePath", adminDongFilePath.toString())
                .addString("newTableName", newTableName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(schemaManager.existTable(newTableName)).isTrue();
        assertViewTargetsTable(newTableName);
    }

}

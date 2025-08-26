package org.songeun.petdongne_server.compare.infrastructure.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.songeun.petdongne_server.compare.domain.AddressTableNameCreator;
import org.songeun.petdongne_server.testSupport.FileUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.songeun.petdongne_server.compare.fixture.AddressFileFixtureFactory.*;
import static org.songeun.petdongne_server.compare.fixture.AddressFileFixtureFactory.unique;

public class AddressUpdateJobTest extends AddressUpdateJobTestSupport {

    @Autowired
    private AddressTableNameCreator tableNameCreator;

    @TempDir
    private Path tempDir;

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

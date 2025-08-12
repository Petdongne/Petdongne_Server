package org.songeun.petdongne_server.compare.infrastructure.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class CreateOrReplaceViewStepTest extends AddressUpdateJobTestSupport {

    @Test
    @DisplayName("지정하는 테이블을 대상으로 뷰를 생성한다.")
    void shouldCreateViewForGivenTable(){
        //given
        String createdTableName = createAddressTable(testAddressTableName());
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("newTableName", createdTableName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("createOrReplaceViewStep", jobParameters);

        //then
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertViewTargetsTable(createdTableName);
    }

    @Test
    @DisplayName("뷰 타겟 테이블을 주어진 테이블을 대상으로 변경한다.")
    void shouldReplaceViewForGivenTable() throws SQLException {
        //given
        String originTableName = createAddressTable(testAddressTableName());
        schemaManager.createOrReplaceView(originTableName);

        String newTargetTableName = createAddressTable("address_test_new");
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("newTableName", newTargetTableName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("createOrReplaceViewStep", jobParameters);

        //then
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertViewTargetsTable(newTargetTableName);
    }

}
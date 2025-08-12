package org.songeun.petdongne_server.compare.infrastructure.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTableStepTest extends AddressUpdateJobTestSupport {

    @Test
    @DisplayName("새로운 주소 테이블을 생성한다.")
    void shouldCreateNewTable() {
        //given
        String addressTableName = testAddressTableName();
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("newTableName", addressTableName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("createNewAddressTableStep", jobParameters);

        //then
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        assertThat(stepExecution.getJobParameters().getString("newTableName")).isEqualTo(addressTableName);
        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(schemaManager.existTable(addressTableName)).isTrue();
    }

    @Test
    @DisplayName("생성하려는 테이블과 동일한 이름의 테이블이 이미 존재하면 실패로 처리한다.")
    void shouldNotCreateNewTableIfTableAlreadyExists() {
        //given
        String alreadyCreatedTableName = createAddressTable(testAddressTableName());
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("newTableName", alreadyCreatedTableName)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("createNewAddressTableStep", jobParameters);

        //then
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.FAILED);
    }

}
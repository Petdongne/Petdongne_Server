package org.songeun.petdongne_server.compare.infrastructure.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.compare.domain.entity.AddressTableMetaData;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

import static org.assertj.core.api.Assertions.*;

class CreateIndexStepConfigTest extends AddressUpdateJobTestSupport{

    @Test
    @DisplayName("인덱스를 생성한다.")
    void shouldCreateIndex(){
        //given
        String addressTable = createAddressTable(testAddressTableName());
        JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
                .addString("newTableName", addressTable)
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("createIndexStep", jobParameters);

        //then
        StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(schemaManager.existGinIndex(addressTable, AddressTableMetaData.getFullAddressGinIndexName())
        ).isTrue();
        assertThat(schemaManager.existGinIndex(addressTable, AddressTableMetaData.getAddressInitialsGinIndexName())
        ).isTrue();
    }

}
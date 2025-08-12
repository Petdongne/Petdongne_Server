package org.songeun.petdongne_server.compare.infrastructure.batch.job;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.domain.AddressTableNameCreator;
import org.songeun.petdongne_server.compare.infrastructure.crawling.event.AddressSyncJobRequestDto;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressSyncJobLauncher {

    private final JobLauncher jobLauncher;
    private final Job updateAddressJob;
    private final AddressTableNameCreator addressTableNameCreator;

    public JobExecution launch(AddressSyncJobRequestDto syncRequestDto) throws JobExecutionException {
        JobParameters jobParams = new JobParametersBuilder()
                .addString("legalDongAddressFilePath", syncRequestDto.getLegaldongFilePath().toString())
                .addString("adminDongAddressFilePath", syncRequestDto.getAdmindongFilePath().toString())
                .addString("newTableName", addressTableNameCreator.createNewTableName())
                .toJobParameters();

        return jobLauncher.run(updateAddressJob, jobParams);
    }

}

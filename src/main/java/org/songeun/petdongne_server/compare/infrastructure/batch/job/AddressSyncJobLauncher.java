package org.songeun.petdongne_server.compare.infrastructure.batch.job;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.infrastructure.crawling.event.AddressSyncJobRequestDto;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressSyncJobLauncher {

    private final JobLauncher jobLauncher;
    private final Job updateAddressJob;

    public JobExecution launch(AddressSyncJobRequestDto syncRequestDto) throws JobExecutionException {
        JobParameters jobParams = new JobParametersBuilder()
                .addString("legalDongAddressFilePath", syncRequestDto.getLegaldongFilePath().toString())
                .addString("adminDongAddressFilePath", syncRequestDto.getAdmindongFilePath().toString())
                .addString("newTableName", "temp") // todo 법정동, 행정동 테이블 이름 생성 및 전달
                .toJobParameters();

        return jobLauncher.run(updateAddressJob, jobParams);
    }

}

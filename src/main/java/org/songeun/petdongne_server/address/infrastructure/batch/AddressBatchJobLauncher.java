package org.songeun.petdongne_server.address.infrastructure.batch;

import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.infrastructure.crawling.event.AddressCrawlingCompletedEvent;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexNameFactory;
import org.songeun.petdongne_server.global.common.TaskExecutor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddressBatchJobLauncher {

    private static final String JOB_SUCCESS_MESSAGE = "주소 인덱싱 작업이 성공적으로 완료되었습니다.";
    private static final String JOB_FAILURE_MESSAGE = "주소 인덱싱 작업 중 문제가 발생했습니다.";
    private static final String BATCH_TASK_EXECUTOR = "batchTaskExecutor";

    private final JobLauncher jobLauncher;
    private final Job addressIndexingJob;
    private final TaskExecutor taskExecutor;

    public AddressBatchJobLauncher(
            JobLauncher jobLauncher,
            @Qualifier("addressIndexingJob") Job addressIndexingJob,
            TaskExecutor taskExecutor
    ) {
        this.jobLauncher = jobLauncher;
        this.addressIndexingJob = addressIndexingJob;
        this.taskExecutor = taskExecutor;
    }

    @EventListener
    @Async(BATCH_TASK_EXECUTOR)
    public void launchAddressIndexingJob(AddressCrawlingCompletedEvent event)  {
        JobParameters jobParams = new JobParametersBuilder()
                .addString("legalDongAddressFilePath", event.getLegaldongFilePath().toString())
                .addString("adminDongAddressFilePath", event.getAdmindongFilePath().toString())
                .addString("newIndexName", AddressIndexNameFactory.createAddressIndexName())
                .toJobParameters();

        taskExecutor.executeWithNotification(
                () -> jobLauncher.run(addressIndexingJob, jobParams),
                JOB_SUCCESS_MESSAGE,
                JOB_FAILURE_MESSAGE
        );
    }

}

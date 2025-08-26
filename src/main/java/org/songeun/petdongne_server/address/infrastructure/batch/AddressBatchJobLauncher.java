package org.songeun.petdongne_server.address.infrastructure.batch;

import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexNameFactory;
import org.songeun.petdongne_server.global.common.NotiTaskExecutor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
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
    private final NotiTaskExecutor notiTaskExecutor;

    public AddressBatchJobLauncher(
            JobLauncher jobLauncher,
            @Qualifier("addressIndexingJob") Job addressIndexingJob,
            NotiTaskExecutor notiTaskExecutor
    ) {
        this.jobLauncher = jobLauncher;
        this.addressIndexingJob = addressIndexingJob;
        this.notiTaskExecutor = notiTaskExecutor;
    }

/*    @EventListener
    @Async(BATCH_TASK_EXECUTOR)*/
    public void launchAddressIndexingJob()  {
        String legal = "C:\\펫동네\\rowData\\jscode20250714\\KIKcd_B.20250714.xlsx";
        String admin = "C:\\펫동네\\rowData\\jscode20250714\\KIKcd_H.20250714.xlsx";
        JobParameters jobParams = new JobParametersBuilder()
                .addString("legalDongAddressFilePath", legal)
                .addString("adminDongAddressFilePath", admin)
                .addString("newIndexName", AddressIndexNameFactory.createAddressIndexName())
                .toJobParameters();

        log.info("잡 런처 실행 중");

        notiTaskExecutor.executeWithNotification(
                () -> jobLauncher.run(addressIndexingJob, jobParams),
                JOB_SUCCESS_MESSAGE,
                JOB_FAILURE_MESSAGE
        );
    }

}

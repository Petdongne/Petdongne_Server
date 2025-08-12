package org.songeun.petdongne_server.address.presentation;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.application.service.AddressCrawlingScheduler;
import org.songeun.petdongne_server.compare.infrastructure.batch.job.AddressSyncJobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scheduler")
public class SchedulerTriggerController {

    private final AddressCrawlingScheduler scheduler;
    private final AddressSyncJobLauncher jobLauncher;

    @PostMapping("/crawl-address")
    public ResponseEntity<String> triggerCrawling() {
        scheduler.processAddressCrawling(); // 수동 실행
        return ResponseEntity.ok("Triggered manually.");
    }

/*    @PostMapping("/address/rdb")
    public ResponseEntity<String> saveAddress() throws JobExecutionException {
        jobLauncher.launch();
        return ResponseEntity.ok("Triggered manually.");
    }*/

}

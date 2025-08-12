package org.songeun.petdongne_server.compare.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.compare.infrastructure.batch.job.AddressSyncJobLauncher;
import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressFileType;
import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressPostIdentifierDto;
import org.songeun.petdongne_server.compare.infrastructure.crawling.event.AddressSyncJobRequestDto;
import org.songeun.petdongne_server.compare.infrastructure.crawling.history.CrawledAddressPost;
import org.songeun.petdongne_server.global.notification.NotifyOnException;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressSyncService {

    private final CrawledAddressPostService crawledAddressPostService;
    private final AddressCrawlingService crawlingService;
    private final AddressSyncJobLauncher syncJobLauncher;

    @NotifyOnException(message = "최신 주소 데이터 업데이트에 실패했습니다.")
    @Scheduled(cron = "0 0 2 * * *")
    public void synchronizeAddressData() {
        verifyAllPreviousCrawlsSynced();
        AddressPostIdentifierDto latest = crawlingService.fetchLatestAddressPost();
        if (isFileSyncRequired(latest)) {
            Map<AddressFileType, Path> toSyncFiles = crawlingService.fetchAddressFile(latest);
            CrawledAddressPost saved = crawledAddressPostService.save(CrawledAddressPost.of(latest.nttId()));

            JobExecution executionResult = null;
            try {
                executionResult = syncJobLauncher.launch(AddressSyncJobRequestDto.of(toSyncFiles));
            } catch (JobExecutionException e) {
                log.error("크롤링 데이터 Sync Job 실패 : {}", e.getMessage());
            } finally {
                updateSyncResult(saved, executionResult);
            }

            if (saved.isSyncFailure()) {
                throw new RuntimeException("sync job failed");
            }
        }
    }

    private void updateSyncResult(CrawledAddressPost addressPost, JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            addressPost.markSyncFailure();
        } else {
            addressPost.markSyncSuccess();
        }

        crawledAddressPostService.save(addressPost);
    }

    private void verifyAllPreviousCrawlsSynced() {
        if (!crawledAddressPostService.isAllSuccessSync()) {
            throw new RuntimeException("크롤링된 주소 데이터 중 데이터베이스에 반영되지 않은 데이터가 있습니다. 먼저 처리해주세요.");
        }
    }

    private boolean isFileSyncRequired(AddressPostIdentifierDto latest) {
        return !crawledAddressPostService.matchesLatestCrawlingHistory(latest);
    }

}

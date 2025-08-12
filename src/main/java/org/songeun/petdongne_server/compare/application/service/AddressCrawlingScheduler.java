package org.songeun.petdongne_server.compare.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressPostIdentifierDto;
import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressCrawlingResult;
import org.songeun.petdongne_server.global.common.NotiTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

/*

CrawlingProcessManager
how to send Message
크롤링





*메시지*
- 크롤링 완료 상태*.
    (1) 이미 했던 것. * 업데이트 미완료 * -> 메시지 = 크롤링 완료 but 해당 주소 업데이트 안됨.
    (2) 새로 한 것. -> 메시지 = 크롤링 완료

- 크롤링 실패 상태*.
    -> 메시지 = 크로링 실패

주소 업데이트
- 완료*
- 실패*
- 여기서 업데이트 상태 체크 > 여기서 알림 전송

 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AddressCrawlingScheduler {

    private final AddressCrawlingService addressCrawlingService;
    private final NotiTaskExecutor notiTaskExecutor;

    @Scheduled(cron = "0 0 2 * * *")
    public void processAddressCrawling() {
        // 크롤링 상태 체크 -> 최신 데이터가 처리되어 있지 않은 상황, throw EX
        // crawlLatestAddressPost
        // isNeedUpdate\
        //  crawlLatestAddressFilesInPost

        notiTaskExecutor.executeWithNotification(
                this::processLatestContentCrawl,
                AddressCrawlingResult.SUCCESS.getMessage(),
                AddressCrawlingResult.FAILURE.getMessage()
        );
    }

    private void processLatestContentCrawl() {
        findNewContent()
                .ifPresentOrElse(
                        this::processNewAddressData,
                        () -> log.info("최신 데이터로, 크롤링을 건너뜁니다.")
                );
        // else -> 크롤링된 주소가 처리되었는지 확인 필요
    }

    private void processNewAddressData(AddressPostIdentifierDto identifier) {
        log.info("새로운 주소 데이터 발견, 크롤링을 시작합니다.");
        addressCrawlingService.fetchAddressFile(identifier);
    }

    private Optional<AddressPostIdentifierDto> findNewContent() {
        AddressPostIdentifierDto latest = addressCrawlingService.fetchLatestAddressPost();

        return addressCrawlingService.matchesLatestCrawlingHistory(latest)
                ? Optional.empty()
                : Optional.of(latest);
    }

}

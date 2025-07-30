package org.songeun.petdongne_server.address.application.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.application.service.AddressCrawlingService;
import org.songeun.petdongne_server.address.infrastructure.crawling.AddressPostIdentifierDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressCrawlingScheduler {

    private final AddressCrawlingService addressCrawlingService;

    @Scheduled(cron = "0 0 2 * * *")
    public void crawlAddressDataIfRequired() {
        findNewContent()
                .ifPresentOrElse(
                        this::processNewAddressData,
                        () -> log.info("최신 데이터로, 크롤링을 건너뜁니다.")
                );
        log.info("주소 업데이트 스케줄링 작업 완료");
    }

    private void processNewAddressData(AddressPostIdentifierDto identifier) {
        log.info("새로운 주소 데이터 발견, 크롤링을 시작합니다.");
        addressCrawlingService.processAddressFile(identifier);
    }

    private Optional<AddressPostIdentifierDto> findNewContent() {
        AddressPostIdentifierDto latest = addressCrawlingService.getLatestAddressPost();

        return addressCrawlingService.matchesLatestCrawlingHistory(latest)
                ? Optional.empty()
                : Optional.of(latest);
    }

}

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
public class AddressUpdateScheduler {

    private final AddressCrawlingService addressCrawlingService;

    @Scheduled(cron = "0 0 2 * * *")
    public void checkAndTriggerUpdate() {
        findUpdateRequiredContent()
                .ifPresentOrElse(
                        this::triggerAddressUpdate,
                        () -> log.info("최신 데이터로, 업데이트를 건너뜁니다.")
                );
        log.info("주소 업데이트 스케줄링 작업 완료");
    }

    private void triggerAddressUpdate(AddressPostIdentifierDto identifier) {
        log.info("새로운 주소 데이터 발견, 업데이트 프로세스를 시작합니다.");
        addressCrawlingService.processAddressFile(identifier);
        log.info("주소 업데이트 프로세스가 비동기적으로 시작되었습니다.");
    }

    private Optional<AddressPostIdentifierDto> findUpdateRequiredContent() {
        AddressPostIdentifierDto latest = addressCrawlingService.getLatestAddressContent();

        return addressCrawlingService.isAlreadyProcessed(latest)
                ? Optional.empty()
                : Optional.of(latest);
    }

}

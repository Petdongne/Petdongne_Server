package org.songeun.petdongne_server.compare.infrastructure.crawling.history;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CrawledAddressPostSyncStatus {

    PENDING("DB 반영 대기 중"),
    SUCCESS("DB 반영 성공"),
    FAILURE("DB 반영 실패");

    private final String description;

}

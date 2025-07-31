package org.songeun.petdongne_server.address.infrastructure.crawling;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AddressCrawlingResult {

    SUCCESS("주소 크롤링 작업이 성공적으로 완료되었습니다."),
    FAILURE("주소 크롤링에 실패했습니다. 로그를 확인해주세요.");

    private final String message;

}

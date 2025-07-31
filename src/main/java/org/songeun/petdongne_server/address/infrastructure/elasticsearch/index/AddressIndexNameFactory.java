package org.songeun.petdongne_server.address.infrastructure.elasticsearch.index;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 주소 인덱스 이름을 제공합니다.
 */
public class AddressIndexNameFactory {

    public static final String ADDRESS_INDEX_ALIAS = "address";
    private static final String DELIMITER = "-";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");

    /**
     * 현재 시각을 기반으로 주소 인덱스의 이름을 생성합니다. <br/>
     * 형식: address-yyyy-MM-dd-HHmmss
     * @return 인덱스 이름 (예: address-2025-07-23-143045)
     */
    public static String createAddressIndexName() {
        return ADDRESS_INDEX_ALIAS + DELIMITER + LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

}

package org.songeun.petdongne_server.compare.domain;

import org.songeun.petdongne_server.compare.domain.entity.AddressTableMetaData;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
class AddressTimeStampTableNameCreator implements AddressTableNameCreator {

    private static final String DELIMITER = "_";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd_HHmmss");

    /**
     * 현재 시각을 기반으로 주소 테이블의 이름을 생성합니다. <br/>
     * 형식: address_yyyy_MM_dd_HHmmss
     * @return 인덱스 이름 (예: address_2025_07_23_143045)
     */
    @Override
    public String createNewTableName() {
        return AddressTableMetaData.VIEW_NAME
                + DELIMITER
                + LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    @Override
    public String createBackupTableName() {
        return AddressTableMetaData.VIEW_NAME
                + DELIMITER
                + LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

}

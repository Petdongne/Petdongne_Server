package org.songeun.petdongne_server.address.infrastructure.crawling;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AddressFileType {
    LEGAL_DONG_ADDRESS("법정동"),
    ADMIN_DONG_ADDRESS("행정동");

    private final String description;

}

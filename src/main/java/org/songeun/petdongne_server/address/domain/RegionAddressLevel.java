package org.songeun.petdongne_server.address.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RegionAddressLevel {

    SIDO("시도"), SIGUNGU("시군구"), EMD("읍면동"), DL("동리");

    private final String description;

}

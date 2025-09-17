package org.songeun.petdongne_server.address.infrastructure.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.ToString;
import org.songeun.petdongne_server.global.common.GeoPoint;

@ToString
@Getter
public class LegalAddressSearchQueryResponseDto {
    private Long id;
    private String fullAddress;
    private GeoPoint centerPoint;

    @QueryProjection
    public LegalAddressSearchQueryResponseDto(Long id, String fullAddress, Double latitude, Double longitude) {
        this.id = id;
        this.fullAddress = fullAddress;
        this.centerPoint = new GeoPoint(latitude, longitude);
    }

}


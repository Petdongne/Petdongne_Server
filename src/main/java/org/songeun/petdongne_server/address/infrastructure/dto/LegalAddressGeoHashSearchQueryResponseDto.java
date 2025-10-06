package org.songeun.petdongne_server.address.infrastructure.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;

@Getter
public class LegalAddressGeoHashSearchQueryResponseDto {

    private final String name;
    private final Double latitude;
    private final Double longitude;
    private final RegionAddressLevel regionAddressLevel;
    private final String geoHash;

    @QueryProjection
    public LegalAddressGeoHashSearchQueryResponseDto(
            String name, Double latitude, Double longitude, RegionAddressLevel regionAddressLevel, String geoHash) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.regionAddressLevel = regionAddressLevel;
        this.geoHash = geoHash;
    }

}

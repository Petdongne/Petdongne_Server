package org.songeun.petdongne_server.address.infrastructure.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;

@ToString
@Getter
public class LegalAddressBoundsSearchQueryResponseDto {

    private final String fullAddress;
    private final Double longitude;
    private final Double latitude;
    private final RegionAddressLevel regionLevel;

    @QueryProjection
    public LegalAddressBoundsSearchQueryResponseDto(String fullAddress, Double longitude, Double latitude, RegionAddressLevel regionLevel) {
        this.fullAddress = fullAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.regionLevel = regionLevel;
    }

}

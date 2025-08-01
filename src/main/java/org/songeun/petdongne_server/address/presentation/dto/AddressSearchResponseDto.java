package org.songeun.petdongne_server.address.presentation.dto;

import lombok.Builder;

@Builder
public record AddressSearchResponseDto(
        String sido,
        String sigungu,
        String eupmyeondong,
        String re,
        String fullAddress
) {

    public static AddressSearchResponseDto of(
            String sido,
            String sigungu,
            String eupmyeondong,
            String re,
            String fullAddress
    ) {
        return AddressSearchResponseDto.builder()
                .sido(sido)
                .sigungu(sigungu)
                .eupmyeondong(eupmyeondong)
                .re(re)
                .fullAddress(fullAddress)
                .build();
    }

}

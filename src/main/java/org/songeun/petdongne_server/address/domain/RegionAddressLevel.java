package org.songeun.petdongne_server.address.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RegionAddressLevel {

    SIDO("시도") {
        @Override
        public int determineGeoHashLength() {
            return 3;
        }
    }, SIGUNGU("시군구") {
        @Override
        public int determineGeoHashLength() {
            return 3;
        }
    }, EMD("읍면동") {
        @Override
        public int determineGeoHashLength() {
            return 5;
        }
    }, DL("동리") {
        @Override
        public int determineGeoHashLength() {
            return 6;
        }
    };

    private final String description;

    public abstract int determineGeoHashLength();

}

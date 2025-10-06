package org.songeun.petdongne_server.address.infrastructure.cache;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;

@EqualsAndHashCode
@Getter
public class LegalAddressCacheKey {

    private String geoHash;
    private RegionAddressLevel level;

    public static LegalAddressCacheKey of(String geoHash, RegionAddressLevel level) {
        return LegalAddressCacheKey.builder()
                .geoHash(geoHash)
                .level(level).build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    private LegalAddressCacheKey(String geoHash, RegionAddressLevel level) {
        this.geoHash = geoHash;
        this.level = level;
    }

}

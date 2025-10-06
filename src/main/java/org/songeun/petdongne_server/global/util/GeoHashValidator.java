package org.songeun.petdongne_server.global.util;

import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class GeoHashValidator {

    public void validGeoHashLength(Set<String> geoHashes, int validLength) {
        for (String geoHash : geoHashes) {
            if (geoHash.length() != validLength) {
                log.error("Invalid geohash {} length: {}",geoHash, geoHash.length());
                log.error("Valid length: {}", validLength);
                throw new BusinessException(GlobalErrorStatus.GEOHASH_LENGTH_INVALID);
            }
        }
    }

}

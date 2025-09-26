package org.songeun.petdongne_server.map.domain;

import org.songeun.petdongne_server.address.domain.RegionAddressLevel;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.songeun.petdongne_server.map.domain.MapErrorStatus.ZOOM_LEVEL_OUT_OF_BOUNDS;

@Component
public class ZoomLevel {

    public RegionAddressLevel toRegionAddressLevel(final Integer kakaoZoomLevel) {
        Assert.notNull(kakaoZoomLevel, "zoomLevel must not be null");
        if (kakaoZoomLevel < 1 || kakaoZoomLevel > 14) {
            throw new BusinessException(ZOOM_LEVEL_OUT_OF_BOUNDS);
        }

        if (kakaoZoomLevel >= 11) {
            return RegionAddressLevel.SIDO;
        } else if (kakaoZoomLevel >= 7) {
            return RegionAddressLevel.SIGUNGU;
        }else {
            return RegionAddressLevel.EMD;
        }
    }

}

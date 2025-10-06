package org.songeun.petdongne_server.map.domain;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;

@Slf4j
@EqualsAndHashCode
public class KakaoZoomLevel implements ZoomLevel{

    private final KakaoZoomLevelCategory levelCategory;
    private final Integer rawValue;

    private KakaoZoomLevel(Integer zoomLevel) {
        this.levelCategory = KakaoZoomLevelCategory.from(zoomLevel);
        this.rawValue = zoomLevel;
    }

    public static KakaoZoomLevel from(Integer zoomLevel) {
        return new KakaoZoomLevel(zoomLevel);
    }

    @Override
    public RegionAddressLevel toRegionAddressLevel() {
        return levelCategory.toRegionAddressLevel();
    }

    @Override
    public boolean isSupportedInCluster() {
        return levelCategory.isSupportedInCluster();
    }

    @Override
    public boolean isSupportedInDetail() {
        return levelCategory.isSupportedInDetail();
    }

}

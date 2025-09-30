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

    @Override
    public Integer determineGeoHashLength() {
        if (levelCategory.isSupportedInCluster()) {
            return levelCategory.determineGeoHashLength();
        }

        if (levelCategory.isSupportedInDetail()) {
            return 6;
        }

        log.error("Unmapped zoom level {}.", rawValue);
        throw new IllegalStateException("줌 레벨 " + rawValue + "은(는) GeoHash로 변환할 수 없습니다.");
    }

}

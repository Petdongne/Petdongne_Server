package org.songeun.petdongne_server.map.domain;

import lombok.AllArgsConstructor;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;
import org.songeun.petdongne_server.global.exception.BusinessException;

import java.util.Arrays;

import java.util.Set;

@AllArgsConstructor
public enum KakaoZoomLevelCategory implements ZoomLevel {

    DETAIL(Set.of(1, 2, 3), RegionAddressLevel.EMD),
    LOCAL(Set.of(4, 5, 6), RegionAddressLevel.EMD),
    AREA(Set.of(7, 8, 9, 10), RegionAddressLevel.SIGUNGU),
    OVERVIEW(Set.of(11, 12, 13, 14), RegionAddressLevel.SIDO);

    private final Set<Integer> zoomLevels;
    private final RegionAddressLevel regionAddressLevel;

    @Override
    public RegionAddressLevel toRegionAddressLevel() {
        return regionAddressLevel;
    }

    @Override
    public boolean isSupportedInCluster() {
        return this != DETAIL;
    }

    @Override
    public boolean isSupportedInDetail() {
        return this == DETAIL;
    }

    // todo 지오 해시 길이 검토 필요
    @Override
    public Integer determineGeoHashLength() {
        if (this == KakaoZoomLevelCategory.OVERVIEW) {
            return 4;
        }

        if (this == KakaoZoomLevelCategory.AREA) {
            return 5;
        }

        if (this == KakaoZoomLevelCategory.LOCAL) {
            return 5;
        }

        throw new RuntimeException("DETAIL 줌 레벨은 KakaoZoomLevel 클래스를 이용하세요");
    }

    public static KakaoZoomLevelCategory from(final int zoomLevel) {
        return Arrays.stream(KakaoZoomLevelCategory.values())
                .filter(level -> level.zoomLevels.contains(zoomLevel))
                .findFirst()
                .orElseThrow(() -> new BusinessException(MapErrorStatus.ZOOM_LEVEL_OUT_OF_BOUNDS));
    }

}

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

    public static KakaoZoomLevelCategory from(final int zoomLevel) {
        return Arrays.stream(KakaoZoomLevelCategory.values())
                .filter(level -> level.zoomLevels.contains(zoomLevel))
                .findFirst()
                .orElseThrow(() -> new BusinessException(MapErrorStatus.ZOOM_LEVEL_OUT_OF_BOUNDS));
    }

}

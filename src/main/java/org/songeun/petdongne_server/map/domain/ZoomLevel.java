package org.songeun.petdongne_server.map.domain;

import org.songeun.petdongne_server.address.domain.RegionAddressLevel;

public interface ZoomLevel {

    RegionAddressLevel toRegionAddressLevel();

    boolean isSupportedInCluster();

    boolean isSupportedInDetail();

    Integer determineGeoHashLength();

}

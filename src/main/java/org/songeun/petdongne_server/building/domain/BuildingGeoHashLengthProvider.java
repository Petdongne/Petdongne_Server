package org.songeun.petdongne_server.building.domain;

import org.springframework.stereotype.Component;

@Component
public class BuildingGeoHashLengthProvider {

    public int getGeoHashLength() {
        return 6;
    }

}

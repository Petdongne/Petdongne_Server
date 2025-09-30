package org.songeun.petdongne_server.global.util;

import com.github.davidmoten.geo.Coverage;
import com.github.davidmoten.geo.GeoHash;
import org.songeun.petdongne_server.map.domain.ZoomLevel;

import java.util.Set;

public class GeoHashUtil {

    public static Set<String> getCoverBoundingBoxHashes(Double minLon, Double minLat, Double maxLon, Double maxLat, ZoomLevel zoomLevel) {
        Coverage coverBoundingBox = GeoHash.coverBoundingBox(maxLat, minLon, minLat, maxLon, zoomLevel.determineGeoHashLength());
        return coverBoundingBox.getHashes();
    }

}

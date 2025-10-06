package org.songeun.petdongne_server.global.util;

import com.github.davidmoten.geo.Coverage;
import com.github.davidmoten.geo.GeoHash;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;
import org.songeun.petdongne_server.map.domain.ZoomLevel;

import java.util.Set;

public class GeoHashUtil {

    public static Set<String> getCoverBoundingBoxHashes(
            Double minLon, Double minLat, Double maxLon, Double maxLat, RegionAddressLevel regionAddressLevel) {
        Coverage coverBoundingBox = GeoHash.coverBoundingBox(
                maxLat, minLon, minLat, maxLon, regionAddressLevel.determineGeoHashLength());
        return coverBoundingBox.getHashes();
    }

    public static Set<String> getCoverBoundingBoxHashes(
            Double minLon, Double minLat, Double maxLon, Double maxLat, int length) {
        Coverage coverBoundingBox = GeoHash.coverBoundingBox(
                maxLat, minLon, minLat, maxLon, length);
        return coverBoundingBox.getHashes();
    }


    public static String getGeoHash(double lat, double lon, int length) {
        return GeoHash.encodeHash(lat, lon, length);
    }

}

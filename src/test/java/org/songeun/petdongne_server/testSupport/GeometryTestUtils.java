package org.songeun.petdongne_server.testSupport;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import static org.geolatte.geom.builder.DSL.*;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

public class GeometryTestUtils {

    public static Point<G2D> defaultPoint() {
        return point(WGS84, g(127.0, 37.5));
    }

}

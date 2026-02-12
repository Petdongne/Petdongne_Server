package org.songeun.petdongne_server.testSupport;

import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;

import static org.geolatte.geom.builder.DSL.*;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

public class GeometryTestUtils {

    public static Point<G2D> defaultPoint() {
        return point(WGS84, g(127.0, 37.5));
    }

    public static MultiPolygon<G2D> multiPolygon(){
        return multipolygon(
                WGS84,
                polygon(
                        ring(
                                g(126.9780, 37.5665),
                                g(127.0000, 37.5665),
                                g(127.0000, 37.5800),
                                g(126.9780, 37.5800),
                                g(126.9780, 37.5665)
                        )
                )
        );
    }

}

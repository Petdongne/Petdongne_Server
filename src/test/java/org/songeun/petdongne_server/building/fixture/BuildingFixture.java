package org.songeun.petdongne_server.building.fixture;

import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.jts.JTS;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.domain.BuildingUsage;
import org.springframework.test.util.ReflectionTestUtils;
import org.locationtech.jts.geom.*;
import org.geolatte.geom.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.geolatte.geom.builder.DSL.*;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;


public class BuildingFixture {

    public static Building createBuilding(
            String name,
            double minLon,
            double minLat,
            double maxLon,
            double maxLat,
            String jibunAddress
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Building> constructor = Building.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Building building = constructor.newInstance();

        ReflectionTestUtils.setField(building, "name", name);
        ReflectionTestUtils.setField(building, "jibunAddress", jibunAddress);
        ReflectionTestUtils.setField(building, "groundFloorCount", 5);
        ReflectionTestUtils.setField(building, "basementFloorCount", 1);
        ReflectionTestUtils.setField(building, "pnu", "testPnu");
        ReflectionTestUtils.setField(building, "roadAddressCode", "testRoadAddressCode");
        ReflectionTestUtils.setField(building, "buildingUsage", BuildingUsage.APARTMENT);

        // polygon
        LinearRingToken<G2D> ring = ring(
                g(minLon, minLat),
                g(maxLon, minLat),
                g(maxLon, maxLat),
                g(minLon, maxLat),
                g(minLon, minLat)
        );

        Polygon<G2D> polygon = polygon(WGS84, ring);
        MultiPolygon<G2D> multipolygon = multipolygon(polygon);
        ReflectionTestUtils.setField(building, "polygon", multipolygon);

        // centerPoint
        org.locationtech.jts.geom.Point centroid = JTS.to(multipolygon).getCentroid();
        double x = centroid.getX();
        double y = centroid.getY();
        Point<G2D> center = point(WGS84, g(x, y));

        ReflectionTestUtils.setField(building, "longitude", x);
        ReflectionTestUtils.setField(building, "latitude", y);
        ReflectionTestUtils.setField(building, "centerPoint", center);

        ReflectionTestUtils.setField(building, "mapIdSource", "TEST_MAP_SOURCE");
        ReflectionTestUtils.setField(building, "clusterYn", Boolean.TRUE);

        return building;
    }

}

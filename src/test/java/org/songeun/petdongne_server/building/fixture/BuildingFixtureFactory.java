package org.songeun.petdongne_server.building.fixture;

import com.github.davidmoten.geo.GeoHash;
import jakarta.persistence.EntityManagerFactory;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.jts.JTS;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.domain.BuildingUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.geolatte.geom.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.IntStream;

import static org.geolatte.geom.builder.DSL.*;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;
import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;


@Profile("test")
@Component
public class BuildingFixtureFactory {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Building makeAndSaveBuilding() {
        return doInJPA(() -> entityManagerFactory, em -> {
            try {
                Building building = createBuilding(
                        "Test Building",
                        "12345678901234", // houseId
                        1, // dongCount
                        100, // householdCount
                        10, // topFloorCount
                        1, // lowestFloorCount
                        "20200101", // approvalDate
                        "Test Jibun Address",
                        "testPnu",
                        "1234567",
                        "Test Road Name",
                        "123", // roadAddressMainNum
                        "45", // roadAddressSubNum
                        "11110", // sigunguCode
                        BuildingUsage.APARTMENT,
                        127.0,
                        37.0
                );
                em.persist(building);
                return building;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    public List<Building> makeAndSaveBuildingsWithin(Double minLon, Double minLat) {
        return doInJPA(() -> entityManagerFactory, em -> {
            List<Building> buildings = IntStream.range(0, 5)
                    .mapToObj(i -> {
                        try {
                            return createBuilding(
                                    "TestBuilding" + i,
                                    "1234567890123" + i, // houseId
                                    1,
                                    100,
                                    10,
                                    1,
                                    "2020010" + i,
                                    "TestJibunAddress" + i,
                                    "testPnu" + i,
                                    "123456" + i,
                                    "Test Road Name" + i,
                                    String.valueOf(123 + i),
                                    String.valueOf(45 + i),
                                    String.valueOf(11110 + i),
                                    BuildingUsage.APARTMENT,
                                    minLon + (i * 0.001),
                                    minLat + (i * 0.001)
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            buildings.forEach(em::persist);
            return buildings;
        });
    }

    public static Building createBuilding(
            String name,
            String houseId,
            Integer dongCount,
            Integer householdCount,
            int topFloorCount,
            int lowestFloorCount,
            String approvalDate,
            String jibunAddress,
            String pnu,
            String roadAddressCode,
            String roadName,
            String roadAddressMainNum,
            String roadAddressSubNum,
            String sigunguCode,
            BuildingUsage buildingUsage,
            double longitude,
            double latitude
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Building> constructor = Building.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Building building = constructor.newInstance();

        ReflectionTestUtils.setField(building, "name", name);
        ReflectionTestUtils.setField(building, "houseId", houseId);
        ReflectionTestUtils.setField(building, "dongCount", dongCount);
        ReflectionTestUtils.setField(building, "householdCount", householdCount);
        ReflectionTestUtils.setField(building, "topFloorCount", topFloorCount);
        ReflectionTestUtils.setField(building, "lowestFloorCount", lowestFloorCount);
        ReflectionTestUtils.setField(building, "approvalDate", approvalDate);
        ReflectionTestUtils.setField(building, "jibunAddress", jibunAddress);
        ReflectionTestUtils.setField(building, "pnu", pnu);
        ReflectionTestUtils.setField(building, "roadAddressCode", roadAddressCode);
        ReflectionTestUtils.setField(building, "roadName", roadName);
        ReflectionTestUtils.setField(building, "roadAddressMainNum", roadAddressMainNum);
        ReflectionTestUtils.setField(building, "roadAddressSubNum", roadAddressSubNum);
        ReflectionTestUtils.setField(building, "sigunguCode", sigunguCode);
        ReflectionTestUtils.setField(building, "buildingUsage", buildingUsage);

        // Define a small polygon around the given coordinates for testing
        // This creates a square polygon with the given longitude and latitude as the bottom-left corner
        double minLon = longitude;
        double minLat = latitude;
        double maxLon = longitude + 0.0001; // Small offset
        double maxLat = latitude + 0.0001; // Small offset

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

        // lon, lat, centerPoint
        org.locationtech.jts.geom.Point centroid = JTS.to(multipolygon).getCentroid();
        double x = centroid.getX();
        double y = centroid.getY();
        Point<G2D> center = point(WGS84, g(x, y));
        ReflectionTestUtils.setField(building, "longitude", x);
        ReflectionTestUtils.setField(building, "latitude", y);
        ReflectionTestUtils.setField(building, "centerPoint", center);

        // geohash
        ReflectionTestUtils.setField(building, "geohash", GeoHash.encodeHash(y, x, 6));

        return building;
    }

}
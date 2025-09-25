package org.songeun.petdongne_server.building.domain;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.hibernate.annotations.Type;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String jibunAddress;

    private int groundFloorCount;

    private int basementFloorCount;

    private String pnu;

    private String roadAddressCode;

    @NotNull
    @Convert(converter = BuildingUsageConverter.class)
    private BuildingUsage buildingUsage;

    @NotNull
    @Column(columnDefinition = "geometry(MultiPolygon, 4326)")
    private MultiPolygon<G2D> polygon;

    @NotNull
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point<G2D> centerPoint;

    private Double longitude;

    private Double latitude;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> details = new HashMap<>();

    private String mapIdSource;

    private Boolean clusterYn;

}

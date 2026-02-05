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
import org.songeun.petdongne_server.global.common.BaseEntity;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Building extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @NotNull
    private String jibunAddress;

    private int groundFloorCount;

    private int basementFloorCount;

    @NotNull
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

    @NotNull
    @Column(columnDefinition = "double precision")
    private Double longitude;

    @NotNull
    @Column(columnDefinition = "double precision")
    private Double latitude;

    @NotNull
    @Column(columnDefinition = "varchar(10)")
    private String geohash;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> details = new HashMap<>();

    @NotNull
    private String mapIdSource;

    @NotNull
    private Boolean clusterYn;

}

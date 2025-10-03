package org.songeun.petdongne_server.address.domain;

import jakarta.persistence.*;
import lombok.*;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.songeun.petdongne_server.global.common.BaseEntity;

// todo gin index, extension check
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class LegalAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String code;

    private String fullAddress; // full name

    private String addressInitials; // init names

    @Embedded
    private LegalAddressParts addressParts;

    @Column(columnDefinition = "geometry(MultiPolygon, 4326)")
    private MultiPolygon<G2D> polygon;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point<G2D> centerPoint;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private RegionAddressLevel regionAddressLevel;

    @Column(length = 5)
    private String geohash;

}

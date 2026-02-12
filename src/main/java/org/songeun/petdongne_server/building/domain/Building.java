package org.songeun.petdongne_server.building.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPolygon;
import org.geolatte.geom.Point;
import org.hibernate.annotations.Comment;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Building extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Comment("단지고유번호")
    @Column(name = "hsmp_innb", length = 14)
    private String houseId;

    @Comment("동수")
    @Column(name = "dong_cnt", columnDefinition = "smallint")
    private Integer dongCount;

    @Comment("세대수")
    @Column(name = "nmhsh", columnDefinition = "smallint")
    private Integer householdCount;

    @Comment("최고층수")
    @Column(name = "top_flct")
    private int topFloorCount;

    @Comment("최저층수")
    @Column(name = "lwst_flct")
    private int lowestFloorCount;

    @Comment("사용승인일자")
    @Column(name = "use_ap_ymd", length = 8)
    private String approvalDate;

    @Comment("지번주소")
    @Column(name = "lnno_adres")
    private String jibunAddress;

    @Comment("필지고유번호")
    @Column(name = "pnu", length = 19)
    private String pnu;

    @Comment("도로명 번호")
    @Column(name = "road_nm_no", length = 7)
    private String roadAddressCode;

    @Comment("도로명")
    @Column(name = "rn", length = 100)
    private String roadName;

    @Comment("도로건물번호 본번")
    @Column(name = "road_hmno", length = 6)
    private String roadAddressMainNum;

    @Comment("도로건물번호 부번")
    @Column(name = "road_vcno", length = 6)
    private String roadAddressSubNum;

    @Comment("시군구코드")
    @Column(name = "sigungu_cd", length = 5)
    private String sigunguCode;

    @NotNull
    @Comment("단지종류코드")
    @Column(name = "hsmp_kn_cd", length = 1)
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
    @Column(length = 10)
    private String geohash;

    public String getApprovalYear() {
        if (this.approvalDate == null || this.approvalDate.length() < 4) {
            if (this.approvalDate != null) { // Log only if not null but too short
                log.warn("approvalDate '{}' is too short (length < 4) for Building ID: {}", this.approvalDate, this.id);
            }
            return null;
        }
        return this.approvalDate.substring(0, 4);
    }

    public String getApprovalMonth() {
        if (this.approvalDate == null || this.approvalDate.length() < 6) {
            if (this.approvalDate != null) { // Log only if not null but too short
                log.warn("approvalDate '{}' is too short (length < 6) for Building ID: {}", this.approvalDate, this.id);
            }
            return null;
        }
        return this.approvalDate.substring(4, 6);
    }

}


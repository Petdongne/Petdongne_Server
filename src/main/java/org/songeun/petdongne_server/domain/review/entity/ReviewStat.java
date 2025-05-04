package org.songeun.petdongne_server.domain.review.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.domain.residentialComplex.entity.ResidentialComplex;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewStat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Integer reviewCount;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("5.00")
    @Column(precision = 3, scale = 2)
    private Double averageRating;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residential_complex_id")
    private ResidentialComplex residentialComplex;

    @Builder
    private ReviewStat(Integer reviewCount, Double averageRating, ResidentialComplex residentialComplex) {
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
        this.residentialComplex = residentialComplex;
    }

}

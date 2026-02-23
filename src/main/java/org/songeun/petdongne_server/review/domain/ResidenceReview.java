package org.songeun.petdongne_server.review.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.songeun.petdongne_server.global.common.BaseEntity;



@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResidenceReview extends BaseEntity {

    private static final int MIN_CONTENT_LENGTH = 50;
    private static final int MAX_CONTENT_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Convert(converter = RatingAttributeConverter.class)
    @Column(name = "rating", nullable = false)
    private Rating rating;

    @NotNull
    @Size(min = MIN_CONTENT_LENGTH, max = MAX_CONTENT_LENGTH)
    private String reviewText;

    @NotNull
    private Integer lastOccupiedYear;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private ResidenceReview(Rating rating, String reviewText, Integer lastOccupiedYear, Building building, User user) {
        this.rating = rating;
        this.reviewText = reviewText;
        this.lastOccupiedYear = lastOccupiedYear;
        this.building = building;
        this.user = user;
    }

}

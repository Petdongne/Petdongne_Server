package org.songeun.petdongne_server.review.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String url;

    @NotNull
    private Boolean isPetPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence_review_id")
    private ResidenceReview review;

    @Builder
    private ReviewPhoto(String url, Boolean isPetPhoto, ResidenceReview review) {
        this.url = url;
        this.isPetPhoto = isPetPhoto;
        this.review = review;
    }

}

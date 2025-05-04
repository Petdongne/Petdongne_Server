package org.songeun.petdongne_server.review.entity;


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
    private Boolean petPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_residence_review_id")
    private PetResidenceReview review;

    @Builder
    private ReviewPhoto(String url, Boolean petPhoto, PetResidenceReview review) {
        this.url = url;
        this.petPhoto = petPhoto;
        this.review = review;
    }

}

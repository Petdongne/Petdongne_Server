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
    private Boolean petPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residence_review_id")
    private ResidenceReview review;

    public static ReviewPhoto of(String url, Boolean petPhoto, ResidenceReview review){

        return ReviewPhoto.builder()
                .url(url)
                .petPhoto(petPhoto)
                .review(review).build();
    }

    @Builder
    private ReviewPhoto(String url, Boolean petPhoto, ResidenceReview review) {
        this.url = url;
        this.petPhoto = petPhoto;
        this.review = review;
    }

}

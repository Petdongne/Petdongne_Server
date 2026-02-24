package org.songeun.petdongne_server.review.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String url;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "residence_review_id", nullable = false)
    private ResidenceReview review;

    @Builder
    private ReviewPhoto(String url, ResidenceReview review) {
        this.url = url;
        this.review = review;
    }

    public static ReviewPhoto of(String url, ResidenceReview review) {
        return ReviewPhoto.builder()
                .url(url)
                .review(review)
                .build();
    }
}

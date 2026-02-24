package org.songeun.petdongne_server.review.infrastructure;

import org.songeun.petdongne_server.review.domain.ResidenceReview;
import org.songeun.petdongne_server.review.domain.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {

    Optional<ReviewPhoto> findByReview(ResidenceReview review);

    List<ReviewPhoto> findAllByReview(ResidenceReview residenceReview);
}

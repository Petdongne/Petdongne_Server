package org.songeun.petdongne_server.review.infrastructure;

import org.songeun.petdongne_server.review.domain.entity.ResidenceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidenceReviewRepository extends JpaRepository<ResidenceReview, Long> {

}

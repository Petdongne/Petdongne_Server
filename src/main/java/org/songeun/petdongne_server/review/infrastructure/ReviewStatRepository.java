package org.songeun.petdongne_server.review.infrastructure;

import org.songeun.petdongne_server.review.domain.entity.ReviewStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewStatRepository extends JpaRepository<ReviewStat, Long> {

}

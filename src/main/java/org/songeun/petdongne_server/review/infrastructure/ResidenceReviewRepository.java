package org.songeun.petdongne_server.review.infrastructure;

import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.review.domain.ResidenceReview;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidenceReviewRepository {

    ResidenceReview save(ResidenceReview review);

    Optional<ResidenceReview> findById(Long id);

    boolean existWith(User user, Building building);

    void deleteAll();
}

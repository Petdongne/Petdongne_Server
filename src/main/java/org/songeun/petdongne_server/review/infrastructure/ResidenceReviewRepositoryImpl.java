package org.songeun.petdongne_server.review.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.review.domain.ResidenceReview;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.songeun.petdongne_server.review.domain.QResidenceReview.residenceReview;

@Repository
@RequiredArgsConstructor
public class ResidenceReviewRepositoryImpl implements ResidenceReviewRepository{

    private final ResidenceReviewJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public ResidenceReview save(ResidenceReview review) {
        return jpaRepository.save(review);
    }

    @Override
    public Optional<ResidenceReview> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existWith(User user, Building building) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(residenceReview)
                .where(residenceReview.user.eq(user),
                        residenceReview.building.eq(building))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}

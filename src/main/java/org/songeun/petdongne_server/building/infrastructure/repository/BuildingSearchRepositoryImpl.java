package org.songeun.petdongne_server.building.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.building.domain.QBuilding;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.dto.QBuildingBoundSearchQueryResponseDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.songeun.petdongne_server.building.domain.QBuilding.building;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildingSearchRepositoryImpl implements BuildingSearchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BuildingBoundSearchQueryResponseDto> findWithinBounds(Double minLon, Double minLat, Double maxLon, Double maxLat) {
        return queryFactory.select(
                        new QBuildingBoundSearchQueryResponseDto(
                                building.id,
                                building.name,
                                building.longitude,
                                building.latitude
                        ))
                .from(building)
                .where(building.longitude.between(minLon, maxLon), building.latitude.between(minLat, maxLat))
                .fetch();
    }

}

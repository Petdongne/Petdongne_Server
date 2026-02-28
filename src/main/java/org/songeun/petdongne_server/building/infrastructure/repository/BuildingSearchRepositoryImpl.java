package org.songeun.petdongne_server.building.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.building.application.dto.BuildingBoundSearchResponseDtoNonGeoHash;
import org.songeun.petdongne_server.building.domain.QBuilding;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.dto.QBuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.dto.QBuildingGeoHashSearchQueryResponseDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.songeun.petdongne_server.building.domain.QBuilding.building;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildingSearchRepositoryImpl implements BuildingSearchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Set<BuildingGeoHashSearchQueryResponseDto> findByGeoHashes(Set<String> geohashes) {
        return new HashSet<>(queryFactory.select(
                        new QBuildingGeoHashSearchQueryResponseDto(
                                building.id,
                                building.name,
                                building.longitude,
                                building.latitude,
                                building.geohash
                        ))
                .from(building)
                .where(building.geohash.in(geohashes))
                .fetch());
    }

    @Override
    public List<BuildingBoundSearchQueryResponseDto> findByBBox(double minLat, double minLng, double maxLat, double maxLng) {
        return queryFactory.select(
                new QBuildingBoundSearchQueryResponseDto(
                        building.id,
                        building.name,
                        building.longitude,
                        building.latitude
                ))
                .from(building)
                .where(building.latitude.between(minLat, maxLat),
                        building.longitude.between(minLng, maxLng))
                .fetch();
    }
}

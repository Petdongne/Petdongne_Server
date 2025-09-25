package org.songeun.petdongne_server.building.infrastructure;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.fixture.BuildingFixtureFactory;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingSearchRepository;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;

@Transactional
class BuildingSearchRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private BuildingSearchRepository searchRepository;

    @Autowired
    private BuildingFixtureFactory fixtureFactory;

    @Test
    @DisplayName("주어진 경계 내에 포함된 빌딩을 찾는다")
    void shouldReturnBuildingWithinBoundary() {
        //given
        Double minLon = 127.0172249;
        Double minLat = 37.4905425;
        Double maxLon = 127.0386825;
        Double maxLat = 37.5024595;

        List<Building> saved = fixtureFactory.makeAndSaveBuildingsWithin(minLon, minLat);

        //when
        List<BuildingBoundSearchQueryResponseDto> result = searchRepository.findWithinBounds(minLon, minLat, maxLon, maxLat);

        //then
        assertThat(result).hasSize(saved.size());
        assertThat(result)
                .extracting("name", "longitude", "latitude")
                .containsExactlyInAnyOrderElementsOf(
                        saved.stream()
                                .map(b -> tuple(b.getName(), b.getLongitude(), b.getLatitude()))
                                .toList()
                );
    }

}
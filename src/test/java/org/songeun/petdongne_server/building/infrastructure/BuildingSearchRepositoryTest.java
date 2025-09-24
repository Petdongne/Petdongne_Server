package org.songeun.petdongne_server.building.infrastructure;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.fixture.BuildingFixture;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingBoundSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingSearchRepository;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;

class BuildingSearchRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private BuildingSearchRepository searchRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    @DisplayName("주어진 경계 내에 포함된 빌딩을 찾는다")
    void shouldReturnBuildingWithinBoundary(){
        //given
        Double minLon = 127.0172249;
        Double minLat = 37.4905425;
        Double maxLon = 127.0386825;
        Double maxLat = 37.5024595;

        List<Building> saved = makeAndSaveBuildingsWithin(minLon, minLat);

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

    private List<Building> makeAndSaveBuildingsWithin(Double minLon, Double minLat) {
        return doInJPA(() -> entityManagerFactory, em -> {
            List<Building> buildings = IntStream.range(0, 5)
                    .mapToObj(i -> {
                        try {
                            return BuildingFixture.createBuilding(
                                    "TestBuilding" + i,
                                    minLon + (i * 0.001),
                                    minLat + (i * 0.001),
                                    minLon + 0.001 + (i * 0.001),
                                    minLat + 0.001 + (i * 0.001),
                                    "TestJibunAddress" + i
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            buildings.forEach(em::persist);
            em.flush();
            return buildings;
        });
    }

}
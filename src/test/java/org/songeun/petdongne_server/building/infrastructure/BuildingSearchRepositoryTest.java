package org.songeun.petdongne_server.building.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.building.fixture.BuildingFixtureFactory;
import org.songeun.petdongne_server.building.infrastructure.dto.BuildingGeoHashSearchQueryResponseDto;
import org.songeun.petdongne_server.building.infrastructure.repository.BuildingSearchRepository;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.songeun.petdongne_server.testSupport.PostgresSQLIntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;

class BuildingSearchRepositoryTest extends PostgresSQLIntegrationTestSupport {

    @Autowired
    private BuildingSearchRepository searchRepository;

    @Autowired
    private BuildingFixtureFactory fixtureFactory;

    @Test
    @DisplayName("GeoHash 집합에 속하는 빌딩을 찾는다")
    void shouldFindBuildingsByGeoHashes(){
        //given
        Double latitude = 126.0172249;
        Double longitude = 36.4905425;
        List<Building> expectedResult = fixtureFactory.makeAndSaveBuildingsWithin(latitude, longitude);
        Set<String> geoHashes = expectedResult.stream().map(Building::getGeohash).collect(Collectors.toSet());

        //when
        Set<BuildingGeoHashSearchQueryResponseDto> result = searchRepository.findByGeoHashes(geoHashes);

        //then
        assertThat(result).hasSize(expectedResult.size());
        assertThat(result)
                .extracting("name", "longitude", "latitude")
                .containsExactlyInAnyOrderElementsOf(
                        expectedResult.stream()
                                .map(b -> tuple(b.getName(), b.getLongitude(), b.getLatitude()))
                                .toList()
                );
    }

}
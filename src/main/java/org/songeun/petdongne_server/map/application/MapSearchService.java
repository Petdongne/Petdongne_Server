package org.songeun.petdongne_server.map.application;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.application.dto.AddressBoundsSearchResponseDto;
import org.songeun.petdongne_server.address.application.service.AddressSearchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapSearchService {

    private final AddressSearchService addressSearchService;

    public List<AddressBoundsSearchResponseDto> searchClustersWithinBounds(
            Double minLon,Double minLat, Double maxLon, Double maxLat, Integer level) {

        return addressSearchService.searchWithinBounds(minLon, minLat, maxLon, maxLat, level);
    }

}

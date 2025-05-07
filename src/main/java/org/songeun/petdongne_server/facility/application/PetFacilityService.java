package org.songeun.petdongne_server.facility.application;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.facility.infrastructure.PetFacilityRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetFacilityService {

    private final PetFacilityRepository facilityRepository;

}

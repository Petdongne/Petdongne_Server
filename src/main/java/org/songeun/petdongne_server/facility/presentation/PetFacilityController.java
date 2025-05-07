package org.songeun.petdongne_server.facility.presentation;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.facility.application.PetFacilityService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PetFacilityController {

    private final PetFacilityService petFacilityService;

}

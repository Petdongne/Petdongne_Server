package org.songeun.petdongne_server.residentialComplex.presentation;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.residentialComplex.application.ResidentialComplexService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResidentialComplexController {

    private final ResidentialComplexService residentialComplexService;

}

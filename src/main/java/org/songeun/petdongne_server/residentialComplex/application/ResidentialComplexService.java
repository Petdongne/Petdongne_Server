package org.songeun.petdongne_server.residentialComplex.application;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.residentialComplex.infrastructure.ResidentialComplexRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResidentialComplexService {

    private final ResidentialComplexRepository residentialComplexRepository;

}

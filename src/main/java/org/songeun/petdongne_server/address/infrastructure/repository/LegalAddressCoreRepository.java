package org.songeun.petdongne_server.address.infrastructure.repository;

import org.songeun.petdongne_server.address.domain.LegalAddress;

import java.util.List;

public interface LegalAddressCoreRepository {

    List<LegalAddress> saveAll(List<LegalAddress> addresses);

    void deleteAll(List<LegalAddress> addresses);

}

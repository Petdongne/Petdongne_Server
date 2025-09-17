package org.songeun.petdongne_server.address.infrastructure.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.domain.LegalAddress;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LegalAddressCoreRepositoryImpl implements LegalAddressCoreRepository {

    private final LegalAddressJpaRepository legalAddressJpaRepository;

    @Override
    @Transactional
    public List<LegalAddress> saveAll(List<LegalAddress> addresses) {
        return legalAddressJpaRepository.saveAll(addresses);
    }

    @Override
    @Transactional
    public void deleteAll(List<LegalAddress> addresses) {
        legalAddressJpaRepository.deleteAll(addresses);
    }

}

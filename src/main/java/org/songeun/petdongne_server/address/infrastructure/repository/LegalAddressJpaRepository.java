package org.songeun.petdongne_server.address.infrastructure.repository;

import org.songeun.petdongne_server.address.domain.LegalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalAddressJpaRepository extends JpaRepository<LegalAddress, Long> {
}

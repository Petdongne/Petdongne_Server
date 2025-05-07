package org.songeun.petdongne_server.facility.infrastructure;

import org.songeun.petdongne_server.facility.domain.PetFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetFacilityRepository extends JpaRepository<PetFacility, Long> {

}

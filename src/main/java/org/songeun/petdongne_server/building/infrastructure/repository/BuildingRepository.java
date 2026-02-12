package org.songeun.petdongne_server.building.infrastructure.repository;

import org.songeun.petdongne_server.building.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
}

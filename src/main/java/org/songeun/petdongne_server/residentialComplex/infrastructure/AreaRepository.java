package org.songeun.petdongne_server.residentialComplex.infrastructure;

import org.songeun.petdongne_server.residentialComplex.domain.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

}

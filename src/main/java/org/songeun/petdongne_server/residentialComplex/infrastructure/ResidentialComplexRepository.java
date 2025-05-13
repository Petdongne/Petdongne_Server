package org.songeun.petdongne_server.residentialComplex.infrastructure;

import org.songeun.petdongne_server.residentialComplex.domain.entity.ResidentialComplex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentialComplexRepository extends JpaRepository<ResidentialComplex, Long> {

}

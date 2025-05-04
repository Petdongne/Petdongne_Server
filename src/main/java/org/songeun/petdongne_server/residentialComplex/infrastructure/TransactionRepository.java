package org.songeun.petdongne_server.residentialComplex.infrastructure;

import org.songeun.petdongne_server.residentialComplex.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}

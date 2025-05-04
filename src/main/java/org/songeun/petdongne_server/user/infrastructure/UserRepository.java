package org.songeun.petdongne_server.user.infrastructure;

import org.songeun.petdongne_server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}

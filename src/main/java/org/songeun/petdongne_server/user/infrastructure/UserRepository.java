package org.songeun.petdongne_server.user.infrastructure;

import jakarta.validation.constraints.NotNull;
import org.songeun.petdongne_server.user.domain.entity.AuthenticationProvider;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifierFromProviderAndAuthenticationProvider(
            @NotNull String identifierFromProvider, @NotNull AuthenticationProvider provider);
}

package org.songeun.petdongne_server.user.infrastructure;

import org.songeun.petdongne_server.user.domain.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

}

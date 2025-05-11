package org.songeun.petdongne_server.user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String nickname;

    @NotNull
    @Size(max = 254)
    private String email;

    @NotNull
    private String providerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @Builder
    private User(String nickname, String email, String providerId, ProfileImage profileImage) {
        this.nickname = nickname;
        this.email = email;
        this.providerId = providerId;
        this.profileImage = profileImage;
    }

}


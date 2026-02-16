package org.songeun.petdongne_server.user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@Table(name = "users")
@Getter
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
    private String identifierFromProvider;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthenticationProvider authenticationProvider;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    public static User of(String nickname, String email, String providerId, AuthenticationProvider provider, ProfileImage profileImage){

        return User.builder()
                .nickname(nickname)
                .email(email)
                .identifierFromProvider(providerId)
                .authenticationProvider(provider)
                .profileImage(profileImage)
                .build();
    }

    @Builder
    private User(Long id, String nickname, String email, String identifierFromProvider, AuthenticationProvider authenticationProvider, ProfileImage profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.identifierFromProvider = identifierFromProvider;
        this.authenticationProvider = authenticationProvider;
        this.profileImage = profileImage;
    }

    public void updateInfo(String nickName, String email) {
        this.nickname = nickName;
        this.email = email;
    }
}


package org.songeun.petdongne_server.user.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("닉네임, 이메일, 제공자 ID, 프로필 이미지로 사용자 인스턴스를 생성한다.")
    void createUser() {
        //given
        String nickname = "펫동네러버";
        String email = "petdong@example.com";
        String providerId = "google_1234";
        ProfileImage profileImage = ProfileImage.of("https://petdongne.com/profile.jpg");

        //when
        User user = User.of(nickname, email, providerId, profileImage);

        //then
        assertThat(user)
                .extracting("nickname", "email", "providerId", "profileImage")
                .containsExactly(nickname, email, providerId, profileImage);
    }

}
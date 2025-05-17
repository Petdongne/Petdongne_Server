package org.songeun.petdongne_server.user.domain.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProfileImageTest {

    @Test
    @DisplayName("이미지 링크로 프로필 이미지를 생성한다.")
    void createProfileImage(){
        //given
        String url = "https://petdongne.com/profile.jpg";

        //when
        ProfileImage profileImage = ProfileImage.of(url);

        //then
        assertThat(profileImage)
                .extracting("url").isEqualTo(url);
    }

}
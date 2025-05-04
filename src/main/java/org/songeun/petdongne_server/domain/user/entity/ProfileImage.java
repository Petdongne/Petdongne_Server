package org.songeun.petdongne_server.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String url;

    @Builder
    private ProfileImage(String url) {
        this.url = url;
    }

}

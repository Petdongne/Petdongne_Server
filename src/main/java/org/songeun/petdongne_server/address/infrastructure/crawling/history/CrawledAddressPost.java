package org.songeun.petdongne_server.address.infrastructure.crawling.history;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CrawledAddressPost {

    /**
     * 주소 파일 게시글의 식별자
     */
    @Id
    private Long nttId;

    public boolean matches(Long newNttId) {
        return Objects.equals(this.nttId, newNttId);
    }

}

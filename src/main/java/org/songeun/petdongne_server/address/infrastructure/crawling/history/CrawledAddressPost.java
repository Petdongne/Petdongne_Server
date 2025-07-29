package org.songeun.petdongne_server.address.infrastructure.crawling.history;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.songeun.petdongne_server.global.common.BaseEntity;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class CrawledAddressPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * 주소 파일 게시글의 식별자
     */
    private Long nttId;

    public static CrawledAddressPost of(Long nttId) {
        return CrawledAddressPost.builder().nttId(nttId).build();
    }

    public boolean matches(Long newNttId) {
        return Objects.equals(this.nttId, newNttId);
    }

}

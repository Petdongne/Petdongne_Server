package org.songeun.petdongne_server.compare.infrastructure.crawling.history;

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

    private CrawledAddressPostSyncStatus syncStatus;

    public static CrawledAddressPost of(Long nttId) {
        return CrawledAddressPost.builder()
                .nttId(nttId)
                .syncStatus(CrawledAddressPostSyncStatus.PENDING)
                .build();
    }

    public boolean matches(Long newNttId) {
        return Objects.equals(this.nttId, newNttId);
    }

    public void markSyncSuccess() {
        if (this.syncStatus != CrawledAddressPostSyncStatus.PENDING) {
            throw new IllegalStateException("Cannot mark success from " + this.syncStatus);
        }
        this.syncStatus = CrawledAddressPostSyncStatus.SUCCESS;
    }

    public void markSyncFailure() {
        if (this.syncStatus != CrawledAddressPostSyncStatus.PENDING) {
            throw new IllegalStateException("Cannot mark failure from " + this.syncStatus);
        }
        this.syncStatus = CrawledAddressPostSyncStatus.FAILURE;
    }

    public boolean isSyncFailure() {
        return this.syncStatus == CrawledAddressPostSyncStatus.FAILURE;
    }

}

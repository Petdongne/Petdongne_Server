package org.songeun.petdongne_server.address.infrastructure.crawling.history;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawledAddressPostRepository extends JpaRepository<CrawledAddressPost, Long> {

    // nttId 컬럼을 기준으로 내림차순 정렬 후 가장 큰 값 하나만 반환, nttId는 최신일 수록 큰 값임
    Optional<CrawledAddressPost> findTopByOrderByNttIdDesc();

}
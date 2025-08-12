package org.songeun.petdongne_server.compare.application.service;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressPostIdentifierDto;
import org.songeun.petdongne_server.compare.infrastructure.crawling.history.CrawledAddressPost;
import org.songeun.petdongne_server.compare.infrastructure.crawling.history.CrawledAddressPostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrawledAddressPostService {

    private final CrawledAddressPostRepository crawledAddressPostRepository;

    public CrawledAddressPost save(CrawledAddressPost crawledAddressPost) {
        return crawledAddressPostRepository.save(crawledAddressPost);
    }

    public Optional<CrawledAddressPost> getLatestCrawledAddressPost() {
        return crawledAddressPostRepository.findTopByOrderByNttIdDesc();
    }

    public boolean isAllSuccessSync() {
        long count = crawledAddressPostRepository.findAll().stream()
                .filter(CrawledAddressPost::isSyncFailure)
                .count();

        return count == 0;
    }

    public boolean matchesLatestCrawlingHistory(AddressPostIdentifierDto latestFoundPost) {
        Optional<CrawledAddressPost> processed = crawledAddressPostRepository.findTopByOrderByNttIdDesc();

        // 처리된 이력이 없음
        if (processed.isEmpty()) {
            return false;
        }

        // 새로운 데이터임
        if (!processed.get().matches(latestFoundPost.nttId())) {
            return false;
        }

        return true;
    }

}

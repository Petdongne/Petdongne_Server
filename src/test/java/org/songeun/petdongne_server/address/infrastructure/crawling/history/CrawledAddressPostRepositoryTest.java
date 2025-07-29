package org.songeun.petdongne_server.address.infrastructure.crawling.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.address.support.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class CrawledAddressPostRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private CrawledAddressPostRepository repository;
    
    @Test
    @DisplayName("최신 게시글(NttId 기준)을 조회한다") // NttId 값이 클수록 최신
    void shouldReturnLargestNttId(){
        //given
        var crawled = createCrawledAddressPosts(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        repository.saveAll(crawled);
        
        //when
        Optional<CrawledAddressPost> result = repository.findTopByOrderByNttIdDesc();

        //then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getNttId()).isEqualTo(9L);
    }

    private List<CrawledAddressPost> createCrawledAddressPosts(Long... ids) {
        return Stream.of(ids)
                .map(CrawledAddressPost::of)
                .toList();
    }
    
}

package org.songeun.petdongne_server.address.infrastructure.crawling.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.compare.infrastructure.crawling.history.CrawledAddressPost;

import static org.assertj.core.api.Assertions.assertThat;

class CrawledAddressPostTest {

    @Test
    @DisplayName("nttId로 크롤링된 주소 게시글 인스턴스를 생성한다.")
    void shouldCreateCrawledAddressPost(){
        //given
        Long nttId = 100L;

        //when
        CrawledAddressPost post = CrawledAddressPost.of(nttId);

        //then
        assertThat(post.getNttId()).isEqualTo(nttId);
    }

    @Test
    @DisplayName("주어진 nttId와 동일한 경우 true를 반환한다.")
    void shouldReturnTrueWhenNttIdIsSame(){
        //given
        Long nttId = 100L;
        CrawledAddressPost post = CrawledAddressPost.of(nttId);

        //when
        boolean matches = post.matches(nttId);

        //then
        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("주어진 nttId와 다른 경우 false를 반환한다.")
    void shouldReturnFalseWhenNttIdIsDifferent(){
        //given
        Long storedNttId = 100L;
        CrawledAddressPost post = CrawledAddressPost.of(storedNttId);

        //when
        Long inputNttId = 200L;
        boolean matches = post.matches(inputNttId);

        //then
        assertThat(matches).isFalse();
    }

}
package org.songeun.petdongne_server.compare.infrastructure.crawling;

import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 주소 데이터 파일의 식별 정보를 담는 클래스입니다.
 * @param bbsId 주소 파일이 있는 게시글의 식별을 위한 값
 * @param nttId 주소 파일이 있는 게시글의 식별을 위한 값
 */
public record AddressPostIdentifierDto(
        String bbsId,
        Long nttId
) {

    public static AddressPostIdentifierDto of(final String bbsId, final Long nttId) {
        StringUtils.hasText(bbsId);
        Objects.requireNonNull(nttId);

        return new AddressPostIdentifierDto(bbsId, nttId);
    }

}

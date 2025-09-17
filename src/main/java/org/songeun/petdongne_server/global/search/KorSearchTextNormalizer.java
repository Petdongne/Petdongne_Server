package org.songeun.petdongne_server.global.search;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class KorSearchTextNormalizer {

    // 한글, 숫자, 공백이 아닌 문자를 찾는 패턴
    private static final Pattern NOT_KOR_NUM_SPACE_PATTERN = Pattern.compile("[^가-힣0-9\\s]");

    // 2개 이상의 연속된 공백을 찾는 패턴
    private static final Pattern MULTIPLE_SPACES_PATTERN = Pattern.compile("\\s{2,}");

    /**
     * 검색어를 정규화합니다. 한글 기반 데이터 검색을 수행하는 경우 사용됩니다.
     * @param text 검색어
     * @return 한글, 숫자, 한 글자 공백만 남은 텍스트
     */
    public static String normalize(String text) {
        String replaced = NOT_KOR_NUM_SPACE_PATTERN.matcher(text).replaceAll(StringUtils.EMPTY);

        return MULTIPLE_SPACES_PATTERN.matcher(replaced).replaceAll(StringUtils.SPACE);
    }

}

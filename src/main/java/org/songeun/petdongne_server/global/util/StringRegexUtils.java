package org.songeun.petdongne_server.global.util;

import java.util.regex.Pattern;

public class StringRegexUtils {

    // 한글, 숫자, 공백이 아닌 문자를 찾는 패턴
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^가-힣0-9\\s]");

    // 2개 이상의 연속된 공백을 찾는 패턴
    private static final Pattern MULTIPLE_SPACES_PATTERN = Pattern.compile("\\s{2,}");

    // 1개 이상의 공백을 찾는 패턴
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    public static final String SPACE = " ";
    public static final String EMPTY_STRING = "";

    /**
     * 한글, 숫자, 그리고 하나의 공백만 남기고 모든 문자를 정리(clean)합니다.
     * <p>
     * 1. 한글, 숫자, 공백을 제외한 모든 특수문자를 제거합니다. </br>
     * 2. 2개 이상의 연속된 공백을 하나의 공백으로 치환합니다.
     *
     * @param text 처리할 원본 문자열
     * @return 정리된(cleaned) 문자열. 입력값이 null인 경우 빈 문자열("") 반환.
     */
    public static String cleanToKorNumSpace(String text) {
        if (text == null) {
            return "";
        }

        String replaced = SPECIAL_CHAR_PATTERN.matcher(text).replaceAll(EMPTY_STRING);
        return MULTIPLE_SPACES_PATTERN.matcher(replaced).replaceAll(SPACE);
    }

    /**
     * 문자열을 공백을 기준으로 분할하여 배열로 반환합니다.
     * <p>
     * @param text 처리할 원본 문자열
     * @return 공백으로 분할된 문자열 배열. 입력값이 null인 경우 빈 배열을 반환.
     */
    public static String[] splitByWhitespace(String text) {
        if (text == null) {
            return new String[0];
        }

        return WHITESPACE_PATTERN.split(text);
    }

}

package org.songeun.petdongne_server.global.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashGenerator {

    /**
     * MD5 해시함수를 이용하여 해시를 생성합니다.
     * @param input 입력 문자열
     * @return hash
     */
    public static String generate(String input) {
        return DigestUtils.md5Hex(input);
    }

}

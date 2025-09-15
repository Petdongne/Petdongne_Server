package org.songeun.petdongne_server.global.search;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WhiteSpaceTokenizer {

    public OrderedTokens tokenize(String text) {
        if (text == null) {
            throw new NullPointerException("Text is null");
        }

        String[] split = StringUtils.split(text, org.apache.commons.lang3.StringUtils.SPACE);
        return OrderedTokens.create(split);
    }

}

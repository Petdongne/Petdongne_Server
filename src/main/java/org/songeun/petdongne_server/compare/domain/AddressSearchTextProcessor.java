package org.songeun.petdongne_server.compare.domain;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.util.StringRegexUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddressSearchTextProcessor {

    private final RegionSynonymResolver regionSynonymResolver;

    public ProcessedSearchText process(String text) {
        if (!StringUtils.hasText(text)) {
            return ProcessedSearchText.ofEmpty();
        }

        String cleanedText = StringRegexUtils.cleanToKorNumSpace(text).trim();
        if (cleanedText.isEmpty()) {
            return ProcessedSearchText.ofEmpty();
        }

        if (cleanedText.length() == 1) {
            return ProcessedSearchText.createForSingleChar(cleanedText);
        }

        String[] keywordsArr = StringRegexUtils.splitByWhitespace(cleanedText);
        List<String> keywordsList = Arrays.stream(keywordsArr)
                .map(regionSynonymResolver::resolve)
                .toList();

        return ProcessedSearchText.createForMultiTokens(keywordsList);
    }

}

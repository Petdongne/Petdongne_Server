package org.songeun.petdongne_server.compare.domain;

import lombok.RequiredArgsConstructor;
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

        String cleanedText = text.replaceAll("[^가-힣0-9\\s]", "").trim();
        if (cleanedText.isEmpty()) {
            return ProcessedSearchText.ofEmpty();
        }

        if (cleanedText.length() == 1) {
            return ProcessedSearchText.createForSingle(cleanedText);
        }

        String[] keywordsArr = cleanedText.split("\\s+");
        List<String> keywordsList = Arrays.stream(keywordsArr)
                .distinct()
                .map(regionSynonymResolver::resolve)
                .toList();

        return ProcessedSearchText.createForMulti(keywordsList);
    }

}

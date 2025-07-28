package org.songeun.petdongne_server.address.infrastructure.crawling;

import lombok.AllArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.crawling.exception.AddressDataCrawlingException;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@AllArgsConstructor
public enum AddressFilePattern {

    LEGAL_DONG_ADDRESS(
            "법정동 주소 파일명 패턴",
            Map.of(AddressFileType.LEGAL_DONG_ADDRESS, Pattern.compile("KIKcd_B\\.\\d+\\.xlsx"))
    ),

    ADMIN_DONG_ADDRESS(
            "행정동 주소 파일명 패턴",
            Map.of(AddressFileType.ADMIN_DONG_ADDRESS, Pattern.compile("KIKcd_H\\.\\d+\\.xlsx")))
    ;

    private final String description;
    private final Map<AddressFileType, Pattern> patternMap;

    private static final List<Map.Entry<AddressFileType, Pattern>> ALL_PATTERNS =
            Arrays.stream(AddressFilePattern.values())
                    .flatMap(p -> p.patternMap.entrySet().stream())
                    .toList();

    /**
     * 주어진 파일 경로 중 법정동, 행정동 주소 패턴과 일치하는 파일 경로를 반환합니다.
     *
     * @param addressFiles 주소 파일 경로
     * @return 법정동, 행정동 주소 패턴과 일치하는 파일 경로
     */
    public static Map<AddressFileType, Path> getMatchedFilesOrThrow(List<Path> addressFiles) {
        Map<AddressFileType, Path> pathMap = new HashMap<>();

        for (Path file : addressFiles) {
            putIfPatternMatch(file, pathMap);
        }

        if (pathMap.isEmpty()) {
            throw new AddressDataCrawlingException("정규식 패턴에 해당하는 주소 파일이 존재하지 않습니다.");
        }

        return pathMap;
    }

    private static void putIfPatternMatch(Path file, Map<AddressFileType, Path> pathMap) {
        for (Map.Entry<AddressFileType, Pattern> entry : ALL_PATTERNS) {
            Pattern pattern = entry.getValue();

            if (matchesPattern(file, pattern)) {
                AddressFileType fileType = entry.getKey();
                pathMap.put(fileType, file);
                break;
            }
        }
    }

    private static boolean matchesPattern(Path file, Pattern pattern) {
        return pattern.matcher(file.getFileName().toString()).matches();
    }

}

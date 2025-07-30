package org.songeun.petdongne_server.address.infrastructure.crawling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.songeun.petdongne_server.address.infrastructure.crawling.exception.AddressDataCrawlingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 최신 법정동, 행정동 주소 데이터 파일을 크롤링합니다.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AddressDataCrawler {

    // nttId 값을 추출하는 정규식
    private static final String NTT_ID_REGEX = ".*nttId=([0-9]+).*";
    private static final Pattern NTT_ID_PATTERN = Pattern.compile(NTT_ID_REGEX);

    //bbsId 값을 추출하는 정규식
    private static final String BBS_ID_REGEX = ".*bbsId=([A-Z0-9_]+).*";
    private static final Pattern BBS_ID_PATTERN = Pattern.compile(BBS_ID_REGEX);

    // 파일 이름을 추출하는 정규식
    public static final String FILENAME_REGEX = "filename=\"([^\"]+)\"";
    private static final Pattern FILENAME_PATTERN = Pattern.compile(FILENAME_REGEX);

    private static final String HTML_HREF_ATTRIBUTE = "href";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

    private static final String ZIP_DOWNLOAD_LINK_SELECTOR = "#print_area > form > div.table_detail_area > dl.download > dd > div > ul > li:nth-child(2) > a";
    private static final String POST_LIST_TABLE_BODY_SELECTOR = "#print_area > div.table_wrap.type_01 > form > table > tbody";
    private static final String TABLE_ROW_SELECTOR = "tr";

    private static final String TARGET_POST_TITLE = "행정기관(행정동) 및 관할구역(법정동)";

    // 타임아웃 설정
    private static final int CONNECTION_TIMEOUT_MS = 10000; // 10초

    private final AddressCrawlingProperties addressCrawlingProperties;

    /**
     * 최신 주소 데이터가 담긴 게시글을 찾습니다.
     * @return 게시글 식별자
     */
    public AddressPostIdentifierDto fetchLatestPostIdentifier() {
        Element latestPost = getLatestAddressPostElement();
        validateNotNull(latestPost, "최신 게시글 요소를 찾지 못했습니다.");

        String postUrl = latestPost.attr(HTML_HREF_ATTRIBUTE);
        validateHasText(postUrl, "게시글의 하이퍼링크를 찾지 못했습니다.");

        return extractIdentifier(postUrl);
    }

    private AddressPostIdentifierDto extractIdentifier(String url) {
        String nttId = extractMatchingGroup(url, NTT_ID_PATTERN, "nttId");
        String bbsId = extractMatchingGroup(url, BBS_ID_PATTERN, "bbsId");

        return AddressPostIdentifierDto.of(bbsId, Long.parseLong(nttId));
    }

    private String extractMatchingGroup(String source, Pattern pattern, String parameterName) {
        Matcher matcher = pattern.matcher(source);

        if (!matcher.find()) {
            log.warn("정규식 패턴 '{}'에서 {}를 추출할 수 없습니다. 소스: '{}'", pattern.pattern(), parameterName, source);
            throw new AddressDataCrawlingException(
                    String.format("주소 데이터 크롤링 실패: %s 파라미터를 추출할 수 없습니다.", parameterName)
            );
        }

        return matcher.group(1);
    }

    /**
     * 주소 데이터가 담긴 zip 파일을 다운로드합니다.
     * 네트워크 오류 시 재시도합니다.
     * @param addressPostDto zip 파일 게시글
     * @return 다운로드 받은 경로
     */
    @Retryable(
            retryFor = {IOException.class, AddressDataCrawlingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 1.5)
    )
    public Path downloadAddressZipFile(AddressPostIdentifierDto addressPostDto) {
        try {
            String postUrl = buildPostUrl(addressPostDto);
            Document postDocument = fetchDocument(postUrl);
            String zipDownloadUrl = extractZipDownloadUrl(postDocument);

            return downloadFile(zipDownloadUrl);
        } catch (IOException e) {
            log.error("주소 데이터 파일 다운로드 중 오류 발생: {}", e.getMessage(), e);
            throw new AddressDataCrawlingException("주소 데이터 파일 다운로드 실패", e);
        }
    }

    @Recover
    public Path recover(AddressDataCrawlingException ex, AddressPostIdentifierDto identifier) {
        log.error("주소 데이터 파일 다운로드 재시도 실패 - 식별자: {}, 오류: {}", identifier, ex.getMessage());
        throw new AddressDataCrawlingException("주소 데이터 파일 다운로드 최종 실패: 모든 재시도 시도가 실패했습니다.", ex);
    }

    private Path downloadFile(String downloadUrl) throws IOException {
        URLConnection connection = createConnection(downloadUrl);
        String fileName = extractFileNameFromResponse(connection);

        return saveFileToLocal(fileName, connection);
    }

    private Path saveFileToLocal(String fileName, URLConnection connection) throws IOException {
        System.out.println("프로퍼티: " + addressCrawlingProperties.getOutputFileSavePath());
        Path outputPath = Paths.get(addressCrawlingProperties.getOutputFileSavePath(), fileName);

        try (InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("파일 다운로드 완료: {}", outputPath);

            return outputPath;
        }
    }

    private String extractFileNameFromResponse(URLConnection connection) {
        String contentDisposition = connection.getHeaderField(CONTENT_DISPOSITION_HEADER);
        validateHasText(contentDisposition, "Content-Disposition 헤더를 찾을 수 없습니다.");

        return extractMatchingGroup(contentDisposition, FILENAME_PATTERN, "filename");
    }

    private URLConnection createConnection(String url) throws IOException {
        try {
            URL downloadUrl = URI.create(url).toURL();
            URLConnection connection = downloadUrl.openConnection();
            connection.setRequestProperty(HEADER_USER_AGENT, addressCrawlingProperties.getUserAgent());
            return connection;
        } catch (MalformedURLException e) {
            throw new AddressDataCrawlingException("잘못된 URL 형식입니다: " + url, e);
        }
    }

    private String extractZipDownloadUrl(Document document) {
        Element zipDownloadLink = document.selectFirst(ZIP_DOWNLOAD_LINK_SELECTOR);
        validateNotNull(zipDownloadLink, "ZIP 파일 다운로드 링크를 찾을 수 없습니다.");

        return zipDownloadLink.absUrl(HTML_HREF_ATTRIBUTE);
    }

    private String buildPostUrl(AddressPostIdentifierDto identifier) {
        return addressCrawlingProperties.getPostBaseUrl() +
                "?bbsId=" + identifier.bbsId() +
                "&nttId=" + identifier.nttId();
    }

    @Nullable
    private Element getLatestAddressPostElement() {
        String listUrl = addressCrawlingProperties.getPostListBaseUrl();

        try {
            Document document = fetchDocument(listUrl);
            Element tableBody = document.selectFirst(POST_LIST_TABLE_BODY_SELECTOR);

            if (tableBody == null) {
                throw new AddressDataCrawlingException("게시글 목록 테이블을 찾을 수 없습니다.");
            }

            Elements rows = tableBody.select(TABLE_ROW_SELECTOR);

            // 최신순으로 정렬된 게시글에서 주소 게시글을 찾습니다.
            for (Element row : rows) {
                if (isAddressPost(row)) {
                    return row.selectFirst("td.l a");
                }
            }

            return null;
        } catch (IOException e) {
            throw new AddressDataCrawlingException("게시글 목록 조회 실패", e);
        }
    }

    /**
     * 주어진 게시글이 원하는 주소 게시글인지 확인합니다.
     */
    private boolean isAddressPost(Element row) {
        Element titleLink = row.selectFirst("td.l a");
        if (titleLink == null) {
            return false;
        }

        String title = titleLink.text();
        return title.contains(TARGET_POST_TITLE);
    }

    private Document fetchDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(addressCrawlingProperties.getUserAgent())
                .timeout(CONNECTION_TIMEOUT_MS)
                .get();
    }

    /**
     * 값이 null이 아닌지 검증합니다.
     */
    private void validateNotNull(Object value, String message) {
        if (value == null) {
            throw new AddressDataCrawlingException(message);
        }
    }

    /**
     * 문자열이 비어있지 않은지 검증합니다.
     */
    private void validateHasText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new AddressDataCrawlingException(message);
        }
    }

}

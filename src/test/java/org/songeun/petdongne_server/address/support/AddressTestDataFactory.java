package org.songeun.petdongne_server.address.support;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.songeun.petdongne_server.global.util.HashGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 테스트 용도의 행정동, 법정동 주소 데이터를 생성합니다.
 */
@Slf4j
public class AddressTestDataFactory {

    public static final int ADDRESS_PART_START_INDEX = 1;
    public static final int ADMIN_ADDRESS_PART_END_INDEX = 3;
    public static final int LEGAL_ADDRESS_PART_END_INDEX = 4;

    private static final List<String> LEGAL_DONG_HEADER = List.of("법정동코드", "시도명", "시군구명", "읍면동명", "동리명", "생성일자", "말소일자");
    private static final List<String> ADMIN_DONG_HEADER = List.of("행정동코드", "시도명", "시군구명", "읍면동명", "생성일자", "말소일자");

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static LegalDongTestData createLegalDongAddressData() {
        return createUniqueLegalDongRows();
    }

    public static AdminDongTestData createAdminDongAddressData() {
        return createUniqueAdminDongRows();
    }

    public static AdminDongTestData createDeletedAdminDongAddressData(LocalDate currentDate) {
        return AdminDongTestData.builder()
                .rows(List.of(
                        ADMIN_DONG_HEADER,
                        List.of("1100000000", "서울특별시", "", "", "19880423", formatter.format(currentDate.minusDays(1))),
                        List.of("1111000000", "서울특별시", "종로구", "", "19880423", formatter.format(currentDate.minusDays(0))),
                        List.of("1129052500", "서울특별시", "성북구", "성북동", "20071230", formatter.format(currentDate.minusDays(2)))
                )).build();
    }


    public static LegalDongTestData createDeletedLegalDongAddressData(LocalDate currentDate) {
        return LegalDongTestData.builder()
                .rows(List.of(
                        LEGAL_DONG_HEADER,
                        List.of("1100000000", "서울특별시", "", "", "", "19880423", formatter.format(currentDate.minusDays(1))),
                        List.of("1111000000", "서울특별시", "종로구", "", "", "19880423", formatter.format(currentDate.minusDays(0))),
                        List.of("1111010100", "서울특별시", "종로구", "청운동", "", "19880423", formatter.format(currentDate.minusDays(2)))
                )).build();
    }

    public static AddressTestData createUniqueAddressData() {
        return AddressTestData.builder()
                .legalAddressRows(createUniqueLegalDongRows())
                .adminAddressRows(createUniqueAdminDongRows())
                .build();
    }

    public static AddressTestData createDuplicatedAddressData() {
        return AddressTestData.builder()
                .legalAddressRows(createDuplicatedLegalDongRows())
                .adminAddressRows(createDuplicatedAdminDongRows())
                .build();
    }

    public static AddressTestData createCorruptedAddressData() {
        return AddressTestData.builder()
                .legalAddressRows(createUniqueLegalDongRows())
                .adminAddressRows(createCorruptedAdminDongRows())
                .build();
    }

    private static LegalDongTestData createUniqueLegalDongRows() {
        return LegalDongTestData.builder()
                .rows(List.of(
                        LEGAL_DONG_HEADER,
                        List.of("1100000000", "서울특별시", "", "", "", "19880423", ""),
                        List.of("1111000000", "서울특별시", "종로구", "", "", "19880423", ""),
                        List.of("1111010100", "서울특별시", "종로구", "청운동", "", "19880423", ""),
                        List.of("1111010200", "서울특별시", "종로구", "신교동", "", "19880423", ""),
                        List.of("1111010300", "서울특별시", "종로구", "궁정동", "", "19880423", ""),
                        List.of("1111010400", "서울특별시", "종로구", "효자동", "", "19880423", ""),
                        List.of("1111010500", "서울특별시", "종로구", "창성동", "", "19880423", ""),
                        List.of("1111010600", "서울특별시", "종로구", "통의동", "", "19880423", ""),
                        List.of("1111010700", "서울특별시", "종로구", "적선동", "", "19880423", ""),
                        List.of("1111010800", "서울특별시", "종로구", "통인동", "", "19880423", ""),
                        List.of("1111010900", "서울특별시", "종로구", "누상동", "", "19880423", ""),
                        List.of("1111011000", "서울특별시", "종로구", "누하동", "", "19880423", "")

                )).build();
    }

    private static AdminDongTestData createUniqueAdminDongRows() {
        return AdminDongTestData.builder()
                .rows(List.of(
                        ADMIN_DONG_HEADER,
                        List.of("4376025000", "충청북도", "괴산군", "괴산읍", "19880423", ""),
                        List.of("4376031000", "충청북도", "괴산군", "감물면", "19880423", ""),
                        List.of("4376032000", "충청북도", "괴산군", "장연면", "19880423", ""),
                        List.of("4376033000", "충청북도", "괴산군", "연풍면", "19880423", ""),
                        List.of("4376034000", "충청북도", "괴산군", "칠성면", "19880423", ""),
                        List.of("4376035000", "충청북도", "괴산군", "문광면", "19880423", ""),
                        List.of("4376036000", "충청북도", "괴산군", "청천면", "19880423", ""),
                        List.of("4376037000", "충청북도", "괴산군", "청안면", "19880423", ""),
                        List.of("4376039000", "충청북도", "괴산군", "사리면", "19880423", ""),
                        List.of("4376040000", "충청북도", "괴산군", "소수면", "19880423", "")
                )).build();
    }

    private static LegalDongTestData createDuplicatedLegalDongRows() {
        return LegalDongTestData.builder()
                .rows(List.of(
                        LEGAL_DONG_HEADER,
                        List.of("1100000000", "서울특별시", "", "", "", "19880423", ""),
                        List.of("1111000000", "서울특별시", "종로구", "", "", "19880423", ""),
                        List.of("1129010100", "서울특별시", "성북구", "성북동", "", "19880423", "")
                )).build();
    }

    private static AdminDongTestData createDuplicatedAdminDongRows() {
        return AdminDongTestData.builder()
                .rows(List.of(
                        ADMIN_DONG_HEADER,
                        List.of("1100000000", "서울특별시", "", "", "19880423", ""),
                        List.of("1111000000", "서울특별시", "종로구", "", "19880423", ""),
                        List.of("1129052500", "서울특별시", "성북구", "성북동", "20071230", "")
                )).build();
    }

    private static AdminDongTestData createCorruptedAdminDongRows() {
        return AdminDongTestData.builder()
                .rows(List.of(
                        ADMIN_DONG_HEADER,
                        List.of("4376030000", "충청북도", "괴산군", "괴산읍", "19880423", ""),
                        List.of("4376031000", "충청북도", "", "감물면", "19880423", ""),
                        List.of("4376032000", "", "괴산군", "장연면", "19880423", ""),
                        List.of("4376033000", "충청북도", "괴산군", "연풍면", "19880423", "")
                )).build();
    }

    @Builder
    @Getter
    public static class AddressTestData {
        private final LegalDongTestData legalAddressRows;
        private final AdminDongTestData adminAddressRows;

        public List<String> extractLegalAddressIds() {
            return legalAddressRows.extractIds();

        }

        public List<String> extractAdminAddressIds() {
            return adminAddressRows.extractIds();
        }

    }

    @Builder
    @Getter
    public static class LegalDongTestData {

        private final List<List<String>> rows;

        public List<String> extractIds() {
            return extractAddresses().stream()
                    .map(HashGenerator::generate)
                    .toList();
        }

        /**
         * 각 행(row)에서 주소 데이터를 추출해 하나의 전체 주소 문자열로 결합합니다. <br/>
         * 이 과정을 모든 행에 대해 수행하여 주소 문자열 리스트를 생성합니다. <br/>
         * 예: "서울특별시", "강남구", "논현동" → "서울특별시 강남구 논현동"
         *
         * @return 주소 문자열 리스트
         */
        public List<String> extractAddresses() {
            return AddressTestDataFactory.extractAddresses(
                    rows, ADDRESS_PART_START_INDEX, LEGAL_ADDRESS_PART_END_INDEX);
        }

        public int count(){
            return rows.size() - 1; // 헤더 제외 카운트
        }

    }

    @Builder
    @Getter
    public static class AdminDongTestData {

        private final List<List<String>> rows;

        public List<String> extractIds() {
            return extractAddresses().stream()
                    .map(HashGenerator::generate)
                    .toList();
        }

        /**
         * 각 행(row)에서 주소 데이터를 추출해 하나의 전체 주소 문자열로 결합합니다. <br/>
         * 이 과정을 모든 행에 대해 수행하여 주소 문자열 리스트를 생성합니다. <br/>
         * 예: "서울특별시", "강남구", "논현동" → "서울특별시 강남구 논현동"
         *
         * @return 주소 문자열 리스트
         */
        public List<String> extractAddresses() {
            return AddressTestDataFactory.extractAddresses(
                    rows, ADDRESS_PART_START_INDEX, ADMIN_ADDRESS_PART_END_INDEX);
        }

        public int count(){
            return rows.size() - 1; // 헤더 제외 카운트
        }

    }

    private static List<String> extractAddresses(List<List<String>> rows, int startIndex, int endIndex) {
        return rows.stream()
                .skip(1) // 헤더 제외
                .map(row -> {
                    List<String> addressParts = IntStream.rangeClosed(startIndex, endIndex)
                            .filter(i -> i < row.size() && !row.get(i).isBlank())
                            .mapToObj(row::get)
                            .toList();
                    return StringUtils.join(addressParts, " ");
                })
                .filter(address -> !address.isBlank())
                .toList();
    }

}

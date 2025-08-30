package org.songeun.petdongne_server.compare.fixture;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.compare.domain.AdminDongAddressParts;
import org.songeun.petdongne_server.compare.domain.LegalDongAddressParts;
import org.songeun.petdongne_server.compare.domain.entity.Address;
import org.songeun.petdongne_server.testSupport.FileUtils;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


/**
 * 테스트 용도의 행정동, 법정동 주소 데이터를 생성합니다.
 * 실제 주소 파일의 구조를 따릅니다.
 */
@Slf4j
public class AddressFileFixtureFactory {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static LegalDongAddressFixture uniqueLegalDong() {
        return uniqueLegalDongRows();
    }

    public static AdminDongAddressFixture uniqueAdminDong() {
        return uniqueAdminDongRows();
    }

    /**
     * todo getAdminAndLegal
     * @return 정상적인 행정동 & 법정동 데이터. 각 데이터는 서로 중복되지 않습니다.
     */
    public static AddressFixtures unique() {
        return AddressFixtures.builder()
                .legalAddresses(uniqueLegalDongRows())
                .adminAddresses(uniqueAdminDongRows())
                .build();
    }

    public static AddressFixtures duplicated() {
        return AddressFixtures.builder()
                .legalAddresses(duplicatedLegalDongRows())
                .adminAddresses(duplicatedAdminDongRows())
                .build();
    }

    public static AddressFixtures corrupted() {
        return AddressFixtures.builder()
                .legalAddresses(uniqueLegalDongRows())
                .adminAddresses(corruptedAdminDongRows())
                .build();
    }

    /**
     * 업데이트 전/후의 행정동 주소 데이터를 생성합니다.
     * @param updateExecutionDate 업데이트 시행일
     * @return 업데이트 전/후 데이터를 모두 포함하는 Fixture
     */
    public static AdminAddressUpdatedFixtures updatedAdminDong(LocalDate updateExecutionDate) {
        return AdminAddressUpdatedFixtures.builder()
                .unUpdatedAddress(uniqueAdminDong())
                .oldAddress(AdminDongAddressFixture.create(
                        List.of(
                                List.of("4146153000", "경기도 용인시", "처인구", "유림동", "20001010", "")))
                        )
                .newAddress(AdminDongAddressFixture.create(
                        List.of(
                                List.of("4146153500", "경기도 용인시", "처인구", "유림1동", formatter.format(updateExecutionDate), ""), // 생성일자 == 업데이트 시행일
                                List.of("4146153600", "경기도 용인시", "처인구", "유림2동", formatter.format(updateExecutionDate), "")))
                        )
                .build();
    }

    /**
     * 업데이트 전/후의 행정동 주소 데이터를 생성 후 반환합니다.
     * @param updateExecutionDate 업데이트 시행일
     * @return 업데이트 전/후 데이터를 모두 포함하는 Fixture
     */
    public static LegalAddressUpdatedFixtures updatedLegalDong(LocalDate updateExecutionDate) {
        return LegalAddressUpdatedFixtures.builder()
                .unUpdatedAddress(uniqueLegalDong())
                .oldAddress(LegalDongAddressFixture.create(
                        List.of(
                                List.of("4784035000", "경상북도", "성주군", "금수면", "19990101", ""),
                                List.of("4784035028", "경상북도", "성주군", "금수면 명천리", "19990101", ""),
                                List.of("4784035029", "경상북도", "성주군", "금수면 광산리", "19990101", ""))
                ))
                .newAddress(LegalDongAddressFixture.create(
                        List.of(
                                List.of("4784035500", "경상북도", "성주군", "금수강산면", formatter.format(updateExecutionDate), ""),
                                List.of("4784035521", "경상북도", "성주군", "금수강산면 명천리", formatter.format(updateExecutionDate), ""),
                                List.of("4784035522", "경상북도", "성주군", "금수강산면 광산리", formatter.format(updateExecutionDate), ""))
                        ))
                .build();
    }

    @Builder
    @Getter
    public static class AdminAddressUpdatedFixtures {

        private final AdminDongAddressFixture unUpdatedAddress;
        private final AdminDongAddressFixture oldAddress;
        private final AdminDongAddressFixture newAddress;

        public AdminDongAddressFixture beforeUpdated() {
            List<List<String>> unchangedAddressRows = unUpdatedAddress.getRows();
            List<List<String>> oldAddressRows = oldAddress.getRows();

            return AdminDongAddressFixture.create(
                    Stream.concat(unchangedAddressRows.stream(), oldAddressRows.stream()).toList()
            );
        }

        public AdminDongAddressFixture afterUpdated() {
            List<List<String>> unchangedAddressRows = unUpdatedAddress.getRows();
            List<List<String>> newAddressRows = newAddress.getRows();

            return AdminDongAddressFixture.create(
                    Stream.concat(unchangedAddressRows.stream(), newAddressRows.stream()).toList()
            );
        }

    }

    @Builder
    @Getter
    public static class LegalAddressUpdatedFixtures {

        private final LegalDongAddressFixture unUpdatedAddress;
        private final LegalDongAddressFixture oldAddress;
        private final LegalDongAddressFixture newAddress;

        public LegalDongAddressFixture beforeUpdated() {
            List<List<String>> unchangedAddressRows = unUpdatedAddress.getRows();
            List<List<String>> oldAddressRows = oldAddress.getRows();

            return LegalDongAddressFixture.create(
                    Stream.concat(unchangedAddressRows.stream(), oldAddressRows.stream()).toList()
            );
        }

        public LegalDongAddressFixture afterUpdated() {
            List<List<String>> unchangedAddressRows = unUpdatedAddress.getRows();
            List<List<String>> newAddressRows = newAddress.getRows();

            return LegalDongAddressFixture.create(
                    Stream.concat(unchangedAddressRows.stream(), newAddressRows.stream()).toList()
            );
        }

    }

    @Builder
    @Getter
    public static class AddressFixtures {

        private final LegalDongAddressFixture legalAddresses;

        private final AdminDongAddressFixture adminAddresses;

    }

    @Builder
    @Getter
    public static class LegalDongAddressFixture {

        private final List<List<String>> rows;

        public static final List<String> LEGAL_DONG_HEADER = List.of("법정동코드", "시도명", "시군구명", "읍면동명", "동리명", "생성일자", "말소일자");
        public static final int CODE_INDEX = 0;
        public static final int SIDO_INDEX = 1;
        public static final int SIGUNGU_INDEX = 2;
        public static final int EUPMYONDONG_INDEX = 3;
        public static final int RE_INDEX = 4;
        public static final int CREATION_DATE_INDEX = 5;

        public static LegalDongAddressFixture create(List<List<String>> rows) {
            return LegalDongAddressFixture.builder()
                    .rows(rows)
                    .build();
        }

        public int rowCount(){
            return rows.size() - 1; // 헤더 제외 카운트
        }

        public List<Address> toAddresses() {
            int count = rowCount();
            List<Address> addresses = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                List<String> row = rows.get(i);
                LegalDongAddressParts addressParts = LegalDongAddressParts.create(
                        row.get(SIDO_INDEX), row.get(SIGUNGU_INDEX), row.get(EUPMYONDONG_INDEX), row.get(RE_INDEX));
                addresses.add(Address.create(
                        row.get(CODE_INDEX), addressParts));
            }

            return addresses;
        }

        public Path toExcelFile(String fileName, Path targetDirectory) throws Exception {
            return FileUtils.createFile(LEGAL_DONG_HEADER, rows, fileName, targetDirectory);
        }

    }

    @Builder
    @Getter
    public static class AdminDongAddressFixture {

        private final List<List<String>> rows;

        public static final List<String> ADMIN_DONG_HEADER = List.of("행정동코드", "시도명", "시군구명", "읍면동명", "생성일자", "말소일자");
        public static final int CODE_INDEX = 0;
        public static final int SIDO_INDEX = 1;
        public static final int SIGUNGU_INDEX = 2;
        public static final int EUPMYONDONG_INDEX = 3;
        public static final int CREATION_DATE_INDEX = 4;

        public static AdminDongAddressFixture create(List<List<String>> rows) {
            return AdminDongAddressFixture.builder()
                    .rows(rows)
                    .build();
        }

        public Path toExcelFile(String fileName, Path targetDirectory) throws Exception {
            return FileUtils.createFile(ADMIN_DONG_HEADER, rows, fileName, targetDirectory);
        }

        public int rowCount(){
            return rows.size() - 1; // 헤더 제외 카운트
        }

        public List<String> getIdList() {
            return rows.stream().map(row -> row.get(0)).toList();
        }

        public List<Address> toAddresses() {
            int count = rowCount();
            List<Address> addresses = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                List<String> row = rows.get(i);
                AdminDongAddressParts addressParts = AdminDongAddressParts.create(
                        row.get(SIDO_INDEX), row.get(SIGUNGU_INDEX), row.get(EUPMYONDONG_INDEX));

                addresses.add(Address.create(
                        row.get(CODE_INDEX), addressParts));
            }

            return addresses;
        }

    }

    private static LegalDongAddressFixture uniqueLegalDongRows() {
        return LegalDongAddressFixture.create(
                List.of(
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
                        List.of("1111011000", "서울특별시", "종로구", "누하동", "", "19880423", ""))
        );
    }

    private static LegalDongAddressFixture duplicatedLegalDongRows() {
        return LegalDongAddressFixture.create(
                List.of(
                        List.of("1100000000", "서울특별시", "", "", "", "19880423", ""),
                        List.of("1111000000", "서울특별시", "종로구", "", "", "19880423", ""),
                        List.of("1129010100", "서울특별시", "성북구", "성북동", "", "19880423", "")
                ));
    }

    public static LegalDongAddressFixture deletedLegalDong(LocalDate currentDate) {
        return LegalDongAddressFixture.create(
                List.of(
                        List.of("1100000000", "서울특별시", "", "", "", "19880423", formatter.format(currentDate.minusDays(1))),
                        List.of("1111000000", "서울특별시", "종로구", "", "", "19880423", formatter.format(currentDate.minusDays(0))),
                        List.of("1111010100", "서울특별시", "종로구", "청운동", "", "19880423", formatter.format(currentDate.minusDays(2)))
                ));
    }

    private static AdminDongAddressFixture uniqueAdminDongRows() {
        return AdminDongAddressFixture.create(
                List.of(
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
                ));
    }

    private static AdminDongAddressFixture duplicatedAdminDongRows() {
        return AdminDongAddressFixture.create(
                List.of(
                        List.of("1100000000", "서울특별시", "", "", "19880423", ""),
                        List.of("1111000000", "서울특별시", "종로구", "", "19880423", ""),
                        List.of("1129052500", "서울특별시", "성북구", "성북동", "20071230", "")
                ));
    }

    public static AdminDongAddressFixture deletedAdminDong(LocalDate currentDate) {
        return AdminDongAddressFixture.create(
                List.of(
                        List.of("1100000000", "서울특별시", "", "", "19880423", formatter.format(currentDate.minusDays(1))),
                        List.of("1111000000", "서울특별시", "종로구", "", "19880423", formatter.format(currentDate.minusDays(0))),
                        List.of("1129052500", "서울특별시", "성북구", "성북동", "20071230", formatter.format(currentDate.minusDays(2)))
                ));
    }

    private static AdminDongAddressFixture corruptedAdminDongRows() {
        return AdminDongAddressFixture.create(
                List.of(
                        List.of("4376030000", "충청북도", "괴산군", "괴산읍", "19880423", ""),
                        List.of("4376031000", "충청북도", "", "감물면", "19880423", ""),
                        List.of("4376032000", "", "괴산군", "장연면", "19880423", ""),
                        List.of("4376033000", "충청북도", "괴산군", "연풍면", "19880423", "")
                ));
    }

}

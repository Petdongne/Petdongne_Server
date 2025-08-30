package org.songeun.petdongne_server.compare.fixture;

import org.songeun.petdongne_server.compare.domain.entity.Address;

import java.util.List;

import static org.songeun.petdongne_server.compare.domain.entity.AddressType.ADMIN_DONG_ADDRESS;
import static org.songeun.petdongne_server.compare.domain.entity.AddressType.LEGAL_DONG_ADDRESS;

public class AddressFixtureFactory {

    private static final List<Address> MAPOGU_FIXTURE = List.of(
            Address.create("12340001", "서울특별시 마포구", "서마", LEGAL_DONG_ADDRESS),
            Address.create("12340002", "서울특별시 마포구 마포동", "서마마", LEGAL_DONG_ADDRESS),
            Address.create("12340003", "서울특별시 마포구 서교동", "서마서", LEGAL_DONG_ADDRESS),
            Address.create("12340004", "서울특별시 마포구 중동", "서마중", LEGAL_DONG_ADDRESS),
            Address.create("12340005", "서울특별시 마포구 서강동", "서마서", ADMIN_DONG_ADDRESS),
            Address.create("12340006", "서울특별시 마포구 아현동", "서마아", LEGAL_DONG_ADDRESS),
            Address.create("12340007", "서울특별시 마포구 공덕동", "서마공", LEGAL_DONG_ADDRESS),
            Address.create("12340008", "서울특별시 마포구 도화동", "서마도", LEGAL_DONG_ADDRESS),
            Address.create("12340009", "서울특별시 마포구 용강동", "서마용", LEGAL_DONG_ADDRESS),
            Address.create("12340010", "서울특별시 마포구 토정동", "서마토", LEGAL_DONG_ADDRESS),
            Address.create("12340011", "서울특별시 마포구 대흥동", "서마대", LEGAL_DONG_ADDRESS),
            Address.create("12340012", "서울특별시 마포구 염리동", "서마염", LEGAL_DONG_ADDRESS),
            Address.create("12340013", "서울특별시 마포구 신수동", "서마신", LEGAL_DONG_ADDRESS),
            Address.create("12340014", "서울특별시 마포구 현석동", "서마현", LEGAL_DONG_ADDRESS),
            Address.create("12340015", "서울특별시 마포구 구수동", "서마구", LEGAL_DONG_ADDRESS),
            Address.create("12340016", "서울특별시 마포구 창전동", "서마창", LEGAL_DONG_ADDRESS),
            Address.create("12340017", "서울특별시 마포구 상수동", "서마상", LEGAL_DONG_ADDRESS),
            Address.create("12340018", "서울특별시 마포구 하중동", "서마하", LEGAL_DONG_ADDRESS),
            Address.create("12340019", "서울특별시 마포구 신정동", "서마신", LEGAL_DONG_ADDRESS),
            Address.create("12340020", "서울특별시 마포구 당인동", "서마당", LEGAL_DONG_ADDRESS)
    );

    private static final List<Address> SEOUL_FIXTURE = List.of(
            Address.create("23450001", "서울특별시", "서", LEGAL_DONG_ADDRESS),
            Address.create("23450002", "서울특별시 중구", "서중", LEGAL_DONG_ADDRESS),
            Address.create("23450003", "서울특별시 서초구", "서서", LEGAL_DONG_ADDRESS),
            Address.create("23450004", "서울특별시 종로구", "서종", LEGAL_DONG_ADDRESS),
            Address.create("23450005", "서울특별시 용산구", "서용", LEGAL_DONG_ADDRESS),
            Address.create("23450006", "서울특별시 성동구", "서성", LEGAL_DONG_ADDRESS),
            Address.create("23450007", "서울특별시 광진구", "서광", LEGAL_DONG_ADDRESS),
            Address.create("23450008", "서울특별시 중랑구", "서중", LEGAL_DONG_ADDRESS),
            Address.create("23450009", "서울특별시 성북구", "서성", LEGAL_DONG_ADDRESS),
            Address.create("23450010", "서울특별시 구로구", "서구", LEGAL_DONG_ADDRESS),
            Address.create("23450011", "서울특별시 종로구 구기동", "서종구", LEGAL_DONG_ADDRESS),
            Address.create("23450012", "서울특별시 광진구 구의동", "서광구", LEGAL_DONG_ADDRESS),
            Address.create("23450013", "서울특별시 은평구 구산동", "서은구", LEGAL_DONG_ADDRESS),
            Address.create("23450014", "서울특별시 마포구 구수동", "서마구", LEGAL_DONG_ADDRESS)
    );

    private static final List<Address> GWANGJU_SI_FIXTURE = List.of(
            Address.create("34560001", "경기도 광주시", "경광", LEGAL_DONG_ADDRESS),
            Address.create("34560002", "경기도 광주시 경안동", "경광경", LEGAL_DONG_ADDRESS),
            Address.create("34560003", "경기도 광주시 삼동", "경광삼", LEGAL_DONG_ADDRESS),
            Address.create("34560004", "경기도 광주시 직동", "경광직", LEGAL_DONG_ADDRESS)
    );

    private static final List<Address> GWANGJU_METROPOLITAN_CITY_FIXTURE = List.of(
            Address.create("45670001", "광주광역시", "광", LEGAL_DONG_ADDRESS),
            Address.create("45670002", "광주광역시 동구", "광동", LEGAL_DONG_ADDRESS),
            Address.create("45670003", "광주광역시 서구", "광서", LEGAL_DONG_ADDRESS),
            Address.create("45670004", "광주광역시 남구", "광남", LEGAL_DONG_ADDRESS),
            Address.create("45670005", "광주광역시 북구", "광북", LEGAL_DONG_ADDRESS)
    );

    public static List<Address> getMapoguFixture() {
        return MAPOGU_FIXTURE;
    }

    public static List<Address> getSeoulFixture() {
        return SEOUL_FIXTURE;
    }

    public static List<Address> getGwangJuSiFixture() {
        return GWANGJU_SI_FIXTURE;
    }

    public static List<Address> getGwangJuMetropolitanCityFixture() {
        return GWANGJU_METROPOLITAN_CITY_FIXTURE;
    }

}

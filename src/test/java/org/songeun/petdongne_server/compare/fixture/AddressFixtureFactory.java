package org.songeun.petdongne_server.compare.fixture;

import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType;
import org.songeun.petdongne_server.compare.domain.Address;

import java.util.List;

import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType.ADMIN_DONG_ADDRESS;
import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType.LEGAL_DONG_ADDRESS;

public class AddressFixtureFactory {

    private static final List<Address> MAPOGU_FIXTURE = List.of(
            Address.create("서울특별시 마포구", "서마", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 마포동", "서마마", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 서교동", "서마서", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 중동", "서마중", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 서강동", "서마서", AddressType.ADMIN_DONG_ADDRESS),
            Address.create("서울특별시 마포구 아현동", "서마아", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 공덕동", "서마공", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 도화동", "서마도", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 용강동", "서마용", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 토정동", "서마토", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 대흥동", "서마대", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 염리동", "서마염", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 신수동", "서마신", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 현석동", "서마현", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 구수동", "서마구", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 창전동", "서마창", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 상수동", "서마상", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 하중동", "서마하", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 신정동", "서마신", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 당인동", "서마당", AddressType.LEGAL_DONG_ADDRESS)
    );

    private static final List<Address> SEOUL_FIXTURE = List.of(
            Address.create("서울특별시", "서", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 중구", "서중", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 서초구", "서서", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 종로구", "서종", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 용산구", "서용", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 성동구", "서성", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 광진구", "서광", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 중랑구", "서중", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 성북구", "서성", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 구로구", "서구", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 종로구 구기동", "서종구", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 광진구 구의동", "서광구", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 은평구 구산동", "서은구", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("서울특별시 마포구 구수동", "서마구", AddressType.LEGAL_DONG_ADDRESS)
    );

    private static final List<Address> GWANGJU_SI_FIXTURE = List.of(
            Address.create("경기도 광주시", "경광", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("경기도 광주시 경안동", "경광경", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("경기도 광주시 삼동", "경광삼", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("경기도 광주시 직동", "경광직", AddressType.LEGAL_DONG_ADDRESS)
    );

    private static final List<Address> GWANGJU_METROPOLITAN_CITY_FIXTURE = List.of(
            Address.create("광주광역시", "광", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("광주광역시 동구", "광동", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("광주광역시 서구", "광서", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("광주광역시 남구", "광남", AddressType.LEGAL_DONG_ADDRESS),
            Address.create("광주광역시 북구", "광북", AddressType.LEGAL_DONG_ADDRESS)
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

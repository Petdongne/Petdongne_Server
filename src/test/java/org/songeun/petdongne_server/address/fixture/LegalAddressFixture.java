package org.songeun.petdongne_server.address.fixture;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.songeun.petdongne_server.address.domain.LegalAddress;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class LegalAddressFixture {

    public static List<LegalAddress> createIncheonAddress() {
        return List.of(
                createLegalAddress("28000001", "인천광역시 중구 신포동", "인중신"),
                createLegalAddress("28000002", "인천광역시 중구 동인천동", "인중동"),
                createLegalAddress("28000003", "인천광역시 중구 항동", "인중항"),
                createLegalAddress("28000004", "인천광역시 남동구 구월동", "인남구"),
                createLegalAddress("28000005", "인천광역시 남동구 간석동", "인남간"),
                createLegalAddress("28000006", "인천광역시 남동구 만수동", "인남만"),
                createLegalAddress("28000007", "인천광역시 연수구 송도동", "인연송"),
                createLegalAddress("28000008", "인천광역시 연수구 옥련동", "인연옥"),
                createLegalAddress("28000009", "인천광역시 연수구 연수동", "인연연"),
                createLegalAddress("28000010", "인천광역시 부평구 부평동", "인부부"),
                createLegalAddress("28000011", "인천광역시 부평구 삼산동", "인부삼"),
                createLegalAddress("28000012", "인천광역시 부평구 청천동", "인부청"),
                createLegalAddress("28000013", "인천광역시 계양구 계산동", "인계계"),
                createLegalAddress("28000014", "인천광역시 계양구 작전동", "인계작"),
                createLegalAddress("28000015", "인천광역시 계양구 효성동", "인계효"),
                createLegalAddress("28000016", "인천광역시 서구 가정동", "인서가"),
                createLegalAddress("28000017", "인천광역시 서구 연희동", "인서연"),
                createLegalAddress("28000018", "인천광역시 서구 석남동", "인서석"),
                createLegalAddress("28000019", "인천광역시 서구 청라동", "인서청"),
                createLegalAddress("28000020", "인천광역시 강화군 강화동", "인강강")
        );
    }


    public static List<LegalAddress> createMapoguLegalAddress() {
        return List.of(
                createLegalAddress("12340001", "서울특별시 마포구", "서마"),
                createLegalAddress("12340002", "서울특별시 마포구 마포동", "서마마"),
                createLegalAddress("12340003", "서울특별시 마포구 서교동", "서마서"),
                createLegalAddress("12340004", "서울특별시 마포구 중동", "서마중"),
                createLegalAddress("12340005", "서울특별시 마포구 서강동", "서마서"),
                createLegalAddress("12340006", "서울특별시 마포구 아현동", "서마아"),
                createLegalAddress("12340007", "서울특별시 마포구 공덕동", "서마공"),
                createLegalAddress("12340008", "서울특별시 마포구 도화동", "서마도"),
                createLegalAddress("12340009", "서울특별시 마포구 용강동", "서마용"),
                createLegalAddress("12340010", "서울특별시 마포구 토정동", "서마토"),
                createLegalAddress("12340011", "서울특별시 마포구 대흥동", "서마대"),
                createLegalAddress("12340012", "서울특별시 마포구 염리동", "서마염"),
                createLegalAddress("12340013", "서울특별시 마포구 신수동", "서마신"),
                createLegalAddress("12340014", "서울특별시 마포구 현석동", "서마현"),
                createLegalAddress("12340015", "서울특별시 마포구 구수동", "서마구"),
                createLegalAddress("12340016", "서울특별시 마포구 창전동", "서마창"),
                createLegalAddress("12340017", "서울특별시 마포구 상수동", "서마상"),
                createLegalAddress("12340018", "서울특별시 마포구 하중동", "서마하"),
                createLegalAddress("12340019", "서울특별시 마포구 신정동", "서마신"),
                createLegalAddress("12340020", "서울특별시 마포구 당인동", "서마당")
        );
    }

    public static LegalAddress createLegalAddress(String code, String fullAddress, String addressInitials) {
        try {
            Constructor<LegalAddress> constructor = LegalAddress.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            LegalAddress obj = constructor.newInstance();

            // code 필드
            Field codeField = LegalAddress.class.getDeclaredField("code");
            codeField.setAccessible(true);
            codeField.set(obj, code);

            // fullAddress 필드
            Field fullAddressField = LegalAddress.class.getDeclaredField("fullAddress");
            fullAddressField.setAccessible(true);
            fullAddressField.set(obj, fullAddress);

            // addressInitials 필드
            Field initialsField = LegalAddress.class.getDeclaredField("addressInitials");
            initialsField.setAccessible(true);
            initialsField.set(obj, addressInitials);

            // point 필드
            Random rand = new Random();
            double baseLon = 126.9015;
            double baseLat = 37.5663;
            double offset = rand.nextDouble();

            Point<G2D> point = DSL.point(
                    CoordinateReferenceSystems.WGS84,
                    new G2D(baseLon + offset, baseLat + offset)
            );

            Field pointField = LegalAddress.class.getDeclaredField("centerPoint");
            pointField.setAccessible(true);
            pointField.set(obj, point);

            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

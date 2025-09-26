package org.songeun.petdongne_server.address.fixture;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.songeun.petdongne_server.address.domain.LegalAddress;
import org.songeun.petdongne_server.address.domain.RegionAddressLevel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class LegalAddressFixture {

    public static List<LegalAddress> createIncheonAddress(double latitude, double longitude) {

        List<String[]> rawAddresses = List.of(
                new String[]{"28000001", "인천광역시 중구 신포동", "인중신"},
                new String[]{"28000002", "인천광역시 중구 동인천동", "인중동"},
                new String[]{"28000003", "인천광역시 중구 항동", "인중항"},
                new String[]{"28000004", "인천광역시 남동구 구월동", "인남구"},
                new String[]{"28000005", "인천광역시 남동구 간석동", "인남간"},
                new String[]{"28000006", "인천광역시 남동구 만수동", "인남만"},
                new String[]{"28000007", "인천광역시 연수구 송도동", "인연송"},
                new String[]{"28000008", "인천광역시 연수구 옥련동", "인연옥"},
                new String[]{"28000009", "인천광역시 연수구 연수동", "인연연"},
                new String[]{"28000010", "인천광역시 부평구 부평동", "인부부"},
                new String[]{"28000011", "인천광역시 부평구 삼산동", "인부삼"},
                new String[]{"28000012", "인천광역시 부평구 청천동", "인부청"},
                new String[]{"28000013", "인천광역시 계양구 계산동", "인계계"},
                new String[]{"28000014", "인천광역시 계양구 작전동", "인계작"},
                new String[]{"28000015", "인천광역시 계양구 효성동", "인계효"},
                new String[]{"28000016", "인천광역시 서구 가정동", "인서가"},
                new String[]{"28000017", "인천광역시 서구 연희동", "인서연"},
                new String[]{"28000018", "인천광역시 서구 석남동", "인서석"},
                new String[]{"28000019", "인천광역시 서구 청라동", "인서청"},
                new String[]{"28000020", "인천광역시 강화군 강화동", "인강강"}
        );

        return IntStream.range(0, rawAddresses.size())
                .mapToObj(i -> {
                    double offset = i * 0.001;
                    String[] r = rawAddresses.get(i);
                    return createLegalAddress(
                            r[0], r[1], r[2],
                            latitude + offset,
                            longitude + offset
                    );
                })
                .toList();
    }


    public static List<LegalAddress> createMapoguLegalAddress(double latitude, double longitude) {

        List<String[]> rawAddresses = List.of(
                new String[]{"12340001", "서울특별시 마포구", "서마"},
                new String[]{"12340002", "서울특별시 마포구 마포동", "서마마"},
                new String[]{"12340003", "서울특별시 마포구 서교동", "서마서"},
                new String[]{"12340004", "서울특별시 마포구 중동", "서마중"},
                new String[]{"12340005", "서울특별시 마포구 서강동", "서마서"},
                new String[]{"12340006", "서울특별시 마포구 아현동", "서마아"},
                new String[]{"12340007", "서울특별시 마포구 공덕동", "서마공"},
                new String[]{"12340008", "서울특별시 마포구 도화동", "서마도"},
                new String[]{"12340009", "서울특별시 마포구 용강동", "서마용"},
                new String[]{"12340010", "서울특별시 마포구 토정동", "서마토"},
                new String[]{"12340011", "서울특별시 마포구 대흥동", "서마대"},
                new String[]{"12340012", "서울특별시 마포구 염리동", "서마염"},
                new String[]{"12340013", "서울특별시 마포구 신수동", "서마신"},
                new String[]{"12340014", "서울특별시 마포구 현석동", "서마현"},
                new String[]{"12340015", "서울특별시 마포구 구수동", "서마구"},
                new String[]{"12340016", "서울특별시 마포구 창전동", "서마창"},
                new String[]{"12340017", "서울특별시 마포구 상수동", "서마상"},
                new String[]{"12340018", "서울특별시 마포구 하중동", "서마하"},
                new String[]{"12340019", "서울특별시 마포구 신정동", "서마신"},
                new String[]{"12340020", "서울특별시 마포구 당인동", "서마당"}
        );

        return IntStream.range(0, rawAddresses.size())
                .mapToObj(i -> {
                    double offset = i * 0.001;
                    String[] r = rawAddresses.get(i);
                    return createLegalAddress(
                            r[0], r[1], r[2],
                            latitude + offset,
                            longitude + offset
                    );
                })
                .toList();
    }

    public static LegalAddress createLegalAddress(String code, String fullAddress, String addressInitials,
                                                  Double latitude, Double longitude) {
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
            Point<G2D> point = DSL.point(
                    CoordinateReferenceSystems.WGS84,
                    new G2D(longitude, latitude)
            );

            Field pointField = LegalAddress.class.getDeclaredField("centerPoint");
            pointField.setAccessible(true);
            pointField.set(obj, point);

            // level 필드
            RegionAddressLevel addressLevel = RegionAddressLevel.EMD;
            Field regionAddressLevel = LegalAddress.class.getDeclaredField("regionAddressLevel");
            regionAddressLevel.setAccessible(true);
            regionAddressLevel.set(obj, addressLevel);

            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

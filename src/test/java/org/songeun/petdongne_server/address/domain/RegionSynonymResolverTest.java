package org.songeun.petdongne_server.address.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Map;

class RegionSynonymResolverTest {

    private final RegionSynonymResolver resolver = new RegionSynonymResolver();

    @Test
    @DisplayName("축약어 입력 시 전체 지역명을 반환한다.")
    void shouldReturnAllRegionNameMappedBySynonym() throws NoSuchFieldException, IllegalAccessException {
        // given
        Field field = RegionSynonymResolver.class.getDeclaredField("regionMap");
        field.setAccessible(true);
        Map<String, String> regionMap = (Map<String, String>) field.get(resolver);

        // when & then
        for (Map.Entry<String, String> entry : regionMap.entrySet()) {
            String keyword = entry.getKey();
            String expected = entry.getValue();

            String result = resolver.resolveOrGet(keyword);

            assertThat(result)
                    .as("'%s' should resolve to '%s'", keyword, expected)
                    .isEqualTo(expected);
        }
    }

    @Test
    @DisplayName("동의어가 존재하지 않으면 원본 문자열을 그대로 반환한다.")
    void shouldReturnOriginInput(){
        String keyword = "강원특별자치도";

        String result = resolver.resolveOrGet(keyword);

        assertThat(result).isEqualTo("강원특별자치도");
    }

}

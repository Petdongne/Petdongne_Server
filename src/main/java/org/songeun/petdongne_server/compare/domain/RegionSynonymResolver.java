package org.songeun.petdongne_server.compare.domain;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RegionSynonymResolver {

    private static Map<String, String> regionMap = Map.ofEntries(
            Map.entry("경남", "경상남도"),
            Map.entry("경북", "경상북도"),
            Map.entry("경북도", "경상북도"),
            Map.entry("서울시", "서울특별시"),
            Map.entry("광주시", "광주"),
            Map.entry("인천시", "인천광역시"),
            Map.entry("대구시", "대구광역시"),
            Map.entry("대전시", "대전광역시"),
            Map.entry("부산시", "부산광역시"),
            Map.entry("세종시", "세종특별자치시"),
            Map.entry("울산시", "울산광역시"),
            Map.entry("전남", "전라남도"),
            Map.entry("전남도", "전라남도"),
            Map.entry("전북", "전북특별자치도"),
            Map.entry("전북도", "전북특별자치도"),
            Map.entry("전라북도", "전북특별자치도"),
            Map.entry("충남", "충청남도"),
            Map.entry("충남도", "충청남도"),
            Map.entry("충북", "충청북도"),
            Map.entry("충북도", "충청북도"),
            Map.entry("강원도", "강원특별자치도"),
            Map.entry("제주도", "제주특별자치도")
    );

    public String resolve(String keyword) {

        return regionMap.getOrDefault(keyword, keyword);
    }

}

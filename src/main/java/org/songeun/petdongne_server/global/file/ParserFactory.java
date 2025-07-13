package org.songeun.petdongne_server.global.file;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.upload.csv.row.ParserUsingOpenCsv;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ParserFactory {

    private final Map<String, ParserConfig<?>> parserConfigMap;

    public <T> Parser<T> createParser(Class<T> clazz) {
        if (clazz == String.class) {
            return (Parser<T>) new CsvHeaderNameParsing();
        }
        ParserConfig<T> parserConfig = (ParserConfig<T>) parserConfigMap.get(clazz.getSimpleName());

        return new ParserUsingOpenCsv<>(parserConfig);
    }

}

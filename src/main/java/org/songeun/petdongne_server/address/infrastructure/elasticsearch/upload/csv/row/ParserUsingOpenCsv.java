package org.songeun.petdongne_server.address.infrastructure.elasticsearch.upload.csv.row;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.file.Parser;
import org.songeun.petdongne_server.global.file.ParserConfig;

import java.io.BufferedReader;
import java.util.List;

@RequiredArgsConstructor
public class ParserUsingOpenCsv<T> implements Parser<T> {

    private final ParserConfig<T> parserConfig;

    @Override
    public List<T> parse(BufferedReader reader) {
        CsvToBean<T> toBean = new CsvToBeanBuilder<T>(reader)
                .withType(parserConfig.getClazz())
                .withMappingStrategy(parserConfig.getMappingStrategy())
                .withIgnoreLeadingWhiteSpace(parserConfig.getIgnoreLeadingWhiteSpace())
                .withSeparator(parserConfig.getSeparator())
                .withFieldAsNull(parserConfig.getNullFieldIndicator())
                .build();

        return toBean.parse();
    }

}

package org.songeun.petdongne_server.global.file;

import com.opencsv.bean.MappingStrategy;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.Getter;

@Getter
public abstract class ParserConfig<T> {

    private final Class<T> clazz;
    private final MappingStrategy<T> mappingStrategy;
    private final Boolean ignoreLeadingWhiteSpace;
    private final Character separator;
    private final CSVReaderNullFieldIndicator nullFieldIndicator;

    public ParserConfig(
            Class<T> clazz,
            MappingStrategy<T> mappingStrategy,
            Boolean ignoreLeadingWhiteSpace,
            Character separator,
            CSVReaderNullFieldIndicator nullFieldIndicator
    ) {
        this.clazz = clazz;
        this.mappingStrategy = mappingStrategy;
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        this.separator = separator;
        this.nullFieldIndicator = nullFieldIndicator;
    }

}

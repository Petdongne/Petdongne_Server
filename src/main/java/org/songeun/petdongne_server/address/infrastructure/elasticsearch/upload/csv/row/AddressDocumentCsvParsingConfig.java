package org.songeun.petdongne_server.address.infrastructure.elasticsearch.upload.csv.row;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.songeun.petdongne_server.global.file.ParserConfig;
import org.springframework.stereotype.Component;

@Component
public class AddressDocumentCsvParsingConfig extends ParserConfig<AddressDocumentCsv> {
    public AddressDocumentCsvParsingConfig() {
        super(
                AddressDocumentCsv.class,
                strategy(),
                true,
                ',',
                CSVReaderNullFieldIndicator.EMPTY_SEPARATORS
        );
    }

    private static MappingStrategy<AddressDocumentCsv> strategy() {
        MappingStrategy<AddressDocumentCsv> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(AddressDocumentCsv.class);

        return mappingStrategy;
    }

}

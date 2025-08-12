package org.songeun.petdongne_server.address.infrastructure.batch.legalDong;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.LegalDongAddressParts;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.factory.AddressDocumentFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LegalDongAddressItemProcessor implements ItemProcessor<LegalDongAddressRow, AddressDocument> {

    private final AddressDocumentFactory addressDocumentFactory;

    @Override
    public AddressDocument process(LegalDongAddressRow item) throws Exception {
        String code = item.getCode();
        String sido = item.getSido();
        String sigungu = item.getSigungu();
        String eupmyeondong = item.getEupmyeondong();
        String re = item.getRe();

        // 말소된 데이터는 처리하지 않음
        if (!item.isExpired(LocalDate.now())) {
            return null;
        }

        LegalDongAddressParts addressParts = LegalDongAddressParts.create(sido, sigungu, eupmyeondong, re);
        return addressDocumentFactory.create(code, addressParts);
    }

}

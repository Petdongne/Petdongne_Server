package org.songeun.petdongne_server.address.infrastructure.batch.adminDong;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AdminDongAddressParts;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.factory.AddressDocumentFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminDongAddressItemProcessor implements ItemProcessor<AdminDongAddressRow, AddressDocument> {

    private final AddressDocumentFactory addressDocumentFactory;

    @Override
    public AddressDocument process(AdminDongAddressRow item) throws Exception {
        String code = item.getCode();
        String sido = item.getSido();
        String sigungu = item.getSigungu();
        String eupmyeondong = item.getEupmyeondong();

        // 말소된 데이터는 처리하지 않음
        if (!item.isValidData(LocalDate.now())) {
            return null;
        }

        AdminDongAddressParts addressParts = AdminDongAddressParts.create(sido, sigungu, eupmyeondong);
        return addressDocumentFactory.create(code, addressParts);
    }

}

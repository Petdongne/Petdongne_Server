package org.songeun.petdongne_server.address.infrastructure.batch.compositeVer;

import org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem.AddressRow;
import org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem.AdmindongType;
import org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem.LegaldongAddressRow;
import org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem.LegaldongType;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.batch.item.ItemProcessor;

import java.util.Set;

public class AddressItemProcessor implements ItemProcessor<AddressRow, AddressDocument> {

    private Set<AddressRow> addressRows;

    @Override
    public AddressDocument process(AddressRow item) throws Exception {
        // duplicate check if 행정동 주소
        if (item instanceof AdmindongType) {
            if (addressRows.remove(item)) {
                return null;
            }
        }

        // 파싱
        AddressDocument document = parse(item);

        // add to set if 법정동 주소
        if (item instanceof LegaldongType) {
            addressRows.add(item);
        }

        return document;
    }

    private AddressDocument parse(AddressRow item){
        if (item instanceof AdmindongType) {

            return AddressDocument.builder()
                    .id(item.getCode())
                    .sido(item.getSido())
                    .sigungu(item.getSigungu())
                    .eupmyeondong(item.getEupmyeondong())
                    .fullAddress(getFullAddress(item))
                    .build();
        }

        if (item instanceof LegaldongType) {
            LegaldongAddressRow legaldongItem = (LegaldongAddressRow) item;

            return AddressDocument.builder()
                    .id(legaldongItem.getCode())
                    .sido(legaldongItem.getSido())
                    .sigungu(legaldongItem.getSigungu())
                    .eupmyeondong(legaldongItem.getEupmyeondong())
                    .re(legaldongItem.getRe().get()) // todo
                    .fullAddress(getFullAddress(legaldongItem))
                    .build();
        }

        return null;
    }

    private String getFullAddress(AddressRow item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getSido());
        sb.append(' ');
        sb.append(item.getSigungu());
        sb.append(' ');
        sb.append(item.getEupmyeondong());
        sb.append(' ');

        return sb.toString();
    }

}

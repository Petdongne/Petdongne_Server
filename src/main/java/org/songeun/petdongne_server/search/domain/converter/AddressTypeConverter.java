package org.songeun.petdongne_server.search.domain.converter;

import com.opencsv.bean.AbstractBeanField;
import org.songeun.petdongne_server.search.domain.document.AddressType;

public class AddressTypeConverter extends AbstractBeanField<AddressType, String> {

    @Override
    protected Object convert(String name) {
        return AddressType.fromName(name);
    }

}
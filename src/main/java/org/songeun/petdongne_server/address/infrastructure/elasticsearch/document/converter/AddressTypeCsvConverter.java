package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.converter;

import com.opencsv.bean.AbstractBeanField;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType;

public class AddressTypeCsvConverter extends AbstractBeanField<AddressType, String> {

    @Override
    protected Object convert(String name) {
        return AddressType.fromName(name);
    }

}
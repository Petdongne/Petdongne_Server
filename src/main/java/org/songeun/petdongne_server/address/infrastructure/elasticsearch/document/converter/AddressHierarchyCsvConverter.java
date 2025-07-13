package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.converter;

import com.opencsv.bean.AbstractBeanField;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressHierarchy;

public class AddressHierarchyCsvConverter extends AbstractBeanField<AddressHierarchy, String> {

    @Override
    protected Object convert(String level) {
        return AddressHierarchy.fromLevel(level);
    }

}
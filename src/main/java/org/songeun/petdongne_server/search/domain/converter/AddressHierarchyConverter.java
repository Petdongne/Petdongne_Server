package org.songeun.petdongne_server.search.domain.converter;

import com.opencsv.bean.AbstractBeanField;
import org.songeun.petdongne_server.search.domain.document.AddressHierarchy;

public class AddressHierarchyConverter extends AbstractBeanField<AddressHierarchy, String> {

    @Override
    protected Object convert(String level) {
        return AddressHierarchy.fromLevel(level);
    }

}
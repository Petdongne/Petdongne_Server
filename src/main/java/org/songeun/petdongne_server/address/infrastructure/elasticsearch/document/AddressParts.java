package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AddressParts {

    private String sido;

    private String sigungu;

    private String eupmyeondong;

    protected abstract List<String> getPartsByOrder();

    public String concatenateParts(String separator){
        return StringUtils.join(getPartsByOrder(), separator);
    }

}

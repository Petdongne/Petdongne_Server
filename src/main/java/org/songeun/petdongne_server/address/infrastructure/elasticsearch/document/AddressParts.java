package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AddressParts {

    private String sido;

    private String sigungu;

    private String eupmyeondong;

    abstract List<String> getPartsByOrder();

}

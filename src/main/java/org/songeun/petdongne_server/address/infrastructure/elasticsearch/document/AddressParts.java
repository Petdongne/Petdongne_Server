package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AddressParts {

    private String sido;

    private String sigungu;

    private String eupmyeondong;

    public String toFullAddress(){
        return concatenateParts(" ")
                .trim()
                .replaceAll("\\s{2,}", " ");

    };

    public String toAddressInitials() {
        return getPartsByOrder().stream()
                .filter(StringUtils::hasText)
                .map(part -> String.valueOf(part.charAt(0)))
                .collect(Collectors.joining());
    }

    public abstract AddressType toAddressType();

    protected abstract List<String> getPartsByOrder();

    // todo convert to protected
    public String concatenateParts(String separator){
        return String.join(separator, getPartsByOrder());
    }

}

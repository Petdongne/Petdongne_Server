package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.Getter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressHierarchy.*;

@Getter
public class LegalDongAddressParts extends AddressParts {

    private String re;

    protected LegalDongAddressParts(String sido, String sigungu, String eupmyeondong, String re) {
        super(sido, sigungu, eupmyeondong);
        this.re = re;
    }

    public static LegalDongAddressParts create(String sido, String sigungu, String eupmyeondong, String re) {
        if (!StringUtils.hasText(sido)) {
            throw new BusinessException(AddressErrorStatus.SIDO_IS_REQUIRED);
        }

/*        if (StringUtils.hasText(eupmyeondong) && !StringUtils.hasText(sigungu)) {
            throw new BusinessException(AddressErrorStatus.SIGUNGU_IS_REQUIRED_IF_EUPMYEONDONG_EXISTS);
        }*/

/*        if (StringUtils.hasText(re) && (!StringUtils.hasText(sigungu) || !StringUtils.hasText(eupmyeondong))) {
            throw new BusinessException(AddressErrorStatus.MID_ADDRESS_IS_REQUIRED_IF_RE_EXISTS);
        }*/

        return new LegalDongAddressParts(sido, sigungu, eupmyeondong, re);
    }

    @Override
    protected List<String> getPartsByOrder() {
        return List.of(
                getSido(),
                getSigungu() == null ? "" : getSigungu(),
                getEupmyeondong() == null ? "" : getEupmyeondong(),
                getRe()  == null ? "" : getRe()
        );
    }

    @Override
    public AddressType toAddressType() {
        return AddressType.LEGAL_DONG_ADDRESS;
    }

}

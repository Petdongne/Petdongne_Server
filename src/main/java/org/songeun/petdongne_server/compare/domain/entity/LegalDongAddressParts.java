package org.songeun.petdongne_server.compare.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@Embeddable
public class LegalDongAddressParts extends AddressParts {

    private String re;

    protected LegalDongAddressParts(String sido, String sigungu, String eupmyeondong, String re) {
        super(sido, sigungu, eupmyeondong);
        this.re = re;
    }

    public LegalDongAddressParts() {

    }


    public static LegalDongAddressParts create(String sido, String sigungu, String eupmyeondong, String re) {
        if (!StringUtils.hasText(sido)) {
            throw new BusinessException(AddressErrorStatus.SIDO_IS_REQUIRED);
        }

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

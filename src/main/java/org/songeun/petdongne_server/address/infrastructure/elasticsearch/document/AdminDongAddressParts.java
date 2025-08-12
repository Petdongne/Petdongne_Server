package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Getter
public class AdminDongAddressParts extends AddressParts {

    protected AdminDongAddressParts(String sido, String sigungu, String eupmyeondong) {
        super(sido, sigungu, eupmyeondong);
    }

    public static AdminDongAddressParts create(String sido, String sigungu, String eupmyeondong) {
        if (!StringUtils.hasText(sido)) {
            throw new BusinessException(AddressErrorStatus.SIDO_IS_REQUIRED);
        }

/*        if (StringUtils.hasText(eupmyeondong) && !StringUtils.hasText(sigungu)) {
            log.error(sido, sigungu, eupmyeondong);
            throw new BusinessException(AddressErrorStatus.SIGUNGU_IS_REQUIRED_IF_EUPMYEONDONG_EXISTS);
        }*/

        return new AdminDongAddressParts(sido, sigungu, eupmyeondong);
    }

    @Override
    protected List<String> getPartsByOrder() {
        return List.of(
                getSido(),
                getSigungu() == null ? "" : getSigungu(),
                getEupmyeondong() == null ? "" : getEupmyeondong()
                );
    }

    @Override
    public AddressType toAddressType() {
        return AddressType.ADMIN_DONG_ADDRESS;
    }

}

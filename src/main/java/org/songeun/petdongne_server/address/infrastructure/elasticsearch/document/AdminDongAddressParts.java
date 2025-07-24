package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.Getter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
public class AdminDongAddressParts extends AddressParts{

    protected AdminDongAddressParts(String sido, String sigungu, String eupmyeondong) {
        super(sido, sigungu, eupmyeondong);
    }

    public static AdminDongAddressParts create(String sido, String sigungu, String eupmyeondong) {
        if (!StringUtils.hasText(sido)) {
            throw new BusinessException(AddressErrorStatus.SIDO_IS_REQUIRED);
        }

        if (StringUtils.hasText(eupmyeondong) && !StringUtils.hasText(sigungu)) {
            throw new BusinessException(AddressErrorStatus.SIGUNGU_IS_REQUIRED_IF_EUPMYEONDONG_EXISTS);
        }

        return new AdminDongAddressParts(sido, sigungu, eupmyeondong);
    }

    @Override
    List<String> getPartsByOrder() {
        return List.of(
                getSido(),
                getSigungu(),
                getEupmyeondong()
        );
    }

}

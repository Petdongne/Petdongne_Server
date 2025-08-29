package org.songeun.petdongne_server.compare.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Getter
@Embeddable
public class AdminDongAddressParts extends AddressParts {

    protected AdminDongAddressParts(String sido, String sigungu, String eupmyeondong) {
        super(sido, sigungu, eupmyeondong);
    }

    public AdminDongAddressParts() {
        super();
    }

    public static AdminDongAddressParts create(String sido, String sigungu, String eupmyeondong) {
        if (!StringUtils.hasText(sido)) {
            throw new BusinessException(AddressErrorStatus.SIDO_IS_REQUIRED);
        }

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

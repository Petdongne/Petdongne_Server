package org.songeun.petdongne_server.address.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LegalAddressParts {

    @NotNull
    private String sido;

    private String sigungu;

    private String eupmyeondong;

    private String li;

    public static LegalAddressParts create(String sido, String sigungu, String eupmyeondong, String li) {
        if (!StringUtils.hasText(sido)) {
            throw new BusinessException(AddressErrorStatus.SIDO_NULL_OR_EMPTY_NOT_ALLOWED);
        }

        return new LegalAddressParts(sido, sigungu, eupmyeondong, li);
    }

}

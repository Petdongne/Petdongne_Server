package org.songeun.petdongne_server.compare.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressParts;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType;
import org.songeun.petdongne_server.compare.domain.converter.AddressTypeConverter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = AddressTableMetaData.VIEW_NAME)
@Immutable
@ToString
@Setter // 성능 테스트 후 제거
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String code;

    @Column(name = AddressTableMetaData.FULL_ADDRESS)
    private String fullAddress;

    @Column(name = AddressTableMetaData.ADDRESS_INITIALS)
    private String addressInitials;

    @Convert(converter = AddressTypeConverter.class)
    private AddressType type;

    public static Address create(
            String code,
            AddressParts addressParts
    ) {
        String fullAddress = addressParts.toFullAddress();
        AddressType type = addressParts.toAddressType();
        String initials = addressParts.toAddressInitials();

        return Address.builder()
                .code(code)
                .fullAddress(fullAddress)
                .addressInitials(initials)
                .type(type)
                .build();
    }

    public static Address create(
            String fullAddress,
            String addressInitials,
            AddressType type
    ) {
        return Address.builder()
                .fullAddress(fullAddress)
                .addressInitials(addressInitials)
                .type(type)
                .build();
    }

}

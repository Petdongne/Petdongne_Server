package org.songeun.petdongne_server.compare.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.songeun.petdongne_server.compare.domain.converter.AddressTypeConverter;
import org.songeun.petdongne_server.global.common.BaseEntity;

// todo gin index, extension check
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = AddressTableMetaData.TABLE_NAME) // UnifiedAddress
@ToString
public class Address extends BaseEntity {

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
            String code,
            String fullAddress,
            String addressInitials,
            AddressType type
    ) {
        return Address.builder()
                .code(code)
                .fullAddress(fullAddress)
                .addressInitials(addressInitials)
                .type(type)
                .build();
    }

}

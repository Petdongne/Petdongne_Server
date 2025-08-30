package org.songeun.petdongne_server.compare.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.compare.domain.converter.AddressTypeConverter;
import org.songeun.petdongne_server.compare.domain.converter.UpdateTypeConverter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdatedAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Integer code;

    @Convert(converter = AddressTypeConverter.class)
    private AddressType addressType;

    @Convert(converter = UpdateTypeConverter.class)
    private UpdateType updateType;

    private String fullAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_official_update_date_id")
    private AddressOfficialUpdateDate officialUpdateDate;

}

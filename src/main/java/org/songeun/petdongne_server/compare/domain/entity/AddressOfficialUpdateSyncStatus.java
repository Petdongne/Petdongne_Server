package org.songeun.petdongne_server.compare.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressOfficialUpdateSyncStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDate syncCheckDate;

    /**
     * 카카오 맵스 (Kakao Maps)에 업데이트된 주소가 반영되었는지 기록
     */
    private Boolean synced;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_official_update_date_id")
    private AddressOfficialUpdateDate officialUpdateDate;

}
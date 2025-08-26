package org.songeun.petdongne_server.compare.infrastructure.repository;

import org.songeun.petdongne_server.compare.domain.Address;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Address는 View 매핑 객체이므로 해당 레포지토리는 조회 용도로만 사용합니다.
 */
public interface AddressSearchJpaRepository extends JpaRepository<Address, Long> {

    @Query(value = """
        SELECT *, similarity(address_initials, :searchText) AS similarity_score
        FROM address
        WHERE address_initials LIKE CONCAT('%', :searchText, '%')
        ORDER BY similarity_score DESC, id ASC
        """,
            nativeQuery = true
    )
    Slice<Address> findByAddressInitials(@Param("searchText") String searchText, Pageable pageable);

    @Query(
            value = """
            SELECT *, similarity(full_address, :fullSearchText) AS similarity_score
            FROM address
            WHERE :andConditions
            ORDER BY similarity_score DESC, id ASC
            """,
            nativeQuery = true
    )
    Slice<Address> searchByKeywords(
            @Param("fullSearchText") String fullSearchText,
            @Param("andConditions") String andConditions,
            Pageable pageable
    );

}

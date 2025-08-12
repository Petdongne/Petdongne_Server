package org.songeun.petdongne_server.compare.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.compare.domain.Address;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AddressRepository {

    private final AddressSearchJpaRepository searchJpaRepository;
    private final AddressInsertRepository addressInsertRepository;
    private final AddressDeleteRepository addressDeleteRepository;

    public List<Address> search(String query, Pageable pageable) {
        /**
         * select *, similarity(full_address, '광교') AS similarity_score from new_address
         * where full_address like '%광교%'
         * ORDER BY hierarchy_level ASC, similarity_score DESC;
         * + 페이징
         */
        return null;
    }

    public Slice<Address> findByAddressInitialsContaining(String query, PageRequest pageRequest) {
        return searchJpaRepository.findByAddressInitials(query, pageRequest);
    }

    public Slice<Address> findByAddressContainingKeywords(List<String> query, PageRequest pageRequest) {
        String fullSearchText = String.join(" ", query);

        return searchJpaRepository.findByFullAddress(fullSearchText, query.toArray(new String[0]), pageRequest);
    }

    public void batchInsert(List<Address> list) {
        addressInsertRepository.batchInsert(list);
    }

    public void deleteAll() {
        addressDeleteRepository.deleteAll();
    }

}

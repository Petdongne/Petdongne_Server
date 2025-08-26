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
import java.util.stream.Collectors;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AddressRepository {

    private final AddressSearchJpaRepository searchJpaRepository;
    private final AddressSearchRepository searchRepository;
    private final AddressInsertRepository insertRepository;
    private final AddressDeleteRepository deleteRepository;

    public List<Address> search(String query, Pageable pageable) {
        /**
         * select *, similarity(full_address, '광교') AS similarity_score from new_address
         * where full_address like '%광교%'
         * ORDER BY hierarchy_level ASC, similarity_score DESC;
         * + 페이징
         */
        return null;
    }

    public Slice<AddressSearchResponse> searchForAddressInitials(String searchText, PageRequest pageRequest) {
        return searchRepository.searchForAddressInitials(searchText, pageRequest);
    }

    public Slice<AddressSearchResponse> searchForAddress(List<String> query, PageRequest pageRequest) {
        return searchRepository.searchForAddress(query, pageRequest);
    }

    public void batchInsert(List<Address> list) {
        insertRepository.batchInsert(list);
    }

    public void deleteAll() {
        deleteRepository.deleteAll();
    }

}

package org.songeun.petdongne_server.compare.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.tools.StringUtils;
import org.songeun.petdongne_server.compare.domain.entity.AddressType;
import org.songeun.petdongne_server.compare.domain.entity.AddressTableMetaData;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class AddressSearchRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public Slice<AddressSearchResponse> searchForAddress(List<String> query, PageRequest pageRequest) {
        if (query == null || query.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageRequest, false);
        }

        String joinedForFullAddress = String.join(" ", query);

        String fullAddressColumnName = AddressTableMetaData.fullAddressColumnName();
        String viewTable = AddressTableMetaData.viewName();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT *, similarity(")
                .append(fullAddressColumnName).append(", :fullAddress) AS similarity_score")
                .append(" FROM ").append(viewTable)
                .append(" WHERE 1=1");

        Map<String, Object> params = new HashMap<>();
        params.put("fullAddress", joinedForFullAddress);

        for (int i = 0; i < query.size(); i++) {
            queryBuilder.append(" AND ")
                    .append(fullAddressColumnName)
                    .append(" LIKE :addressPart").append(i);

            params.put("addressPart" + i, "%" + query.get(i) + "%");
        }

        queryBuilder.append(" ORDER BY similarity_score DESC, id ASC");
        queryBuilder.append(" LIMIT :limit OFFSET :offset");

        params.put("limit", pageRequest.getPageSize());
        params.put("offset", pageRequest.getOffset());

        try {
            List<AddressSearchResponse> results = namedJdbcTemplate.query(
                    queryBuilder.toString(),
                    params,
                    (rs, rowNum) -> new AddressSearchResponse(
                            rs.getLong("id"),
                            rs.getString("full_address"),
                            AddressType.fromName(rs.getString("type")),
                            rs.getString("address_initials")
                    )
            );

            boolean hasNext = results.size() == pageRequest.getPageSize();

            return new SliceImpl<>(results, pageRequest, hasNext);
        } catch (DataAccessException e) {
            throw new RuntimeException("주소 검색 중 오류가 발생했습니다.", e);
        }
    }

    public Slice<AddressSearchResponse> searchForAddressInitials(String searchText, PageRequest pageRequest) {
        if (StringUtils.isBlank(searchText)) {
            return new SliceImpl<>(Collections.emptyList(), pageRequest, false);
        }

        if (searchText.length() != 1) {
            throw new RuntimeException("검색어 사이즈가 1이 아닙니다.");
        }

        String initialsColumnName = AddressTableMetaData.addressInitialsColumnName();
        String viewTable = AddressTableMetaData.viewName();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT *, similarity(")
                .append(initialsColumnName).append(", :initialsColumn) AS similarity_score")
                .append(" FROM ").append(viewTable)
                .append(" WHERE ").append(initialsColumnName).append(" LIKE :initialsColumn")
                .append(" ORDER BY similarity_score DESC, id ASC")
                .append(" LIMIT :limit OFFSET :offset");

        Map<String, Object> params = new HashMap<>();
        params.put("initialsColumn", "%"+searchText+"%");
        params.put("limit", pageRequest.getPageSize());
        params.put("offset", pageRequest.getOffset());

        try {
            List<AddressSearchResponse> results = namedJdbcTemplate.query(
                    queryBuilder.toString(),
                    params,
                    (rs, rowNum) -> new AddressSearchResponse(
                            rs.getLong("id"),
                            rs.getString("full_address"),
                            AddressType.fromName(rs.getString("type")),
                            rs.getString("address_initials")
                    )
            );

            boolean hasNext = results.size() == pageRequest.getPageSize();

            return new SliceImpl<>(results, pageRequest, hasNext);
        } catch (DataAccessException e) {
            throw new RuntimeException("주소 검색 중 오류가 발생했습니다.", e);
        }
    }

}

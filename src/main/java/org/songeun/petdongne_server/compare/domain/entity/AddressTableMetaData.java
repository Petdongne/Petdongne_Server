package org.songeun.petdongne_server.compare.domain.entity;

public class AddressTableMetaData {

    // 테이블 이름
    static final String TABLE_NAME = "address";

    // 컬럼 이름
    static final String ID = "id";
    static final String CODE = "code";
    static final String FULL_ADDRESS = "full_address";
    static final String ADDRESS_INITIALS = "address_initials";
    static final String TYPE = "type";

    // 인덱스 이름
    static final String FULL_ADDRESS_GIN_INDEX = "gin_full_address_idx";
    static final String ADDRESS_INITIALS_GIN_INDEX = "gin_address_initials_idx";

    public static String viewName() {
        return TABLE_NAME;
    }

    public static String idColumnName() {
        return ID;
    }

    public static String codeColumnName() {
        return CODE;}

    public static String fullAddressColumnName() {
        return FULL_ADDRESS;
    }

    public static String addressInitialsColumnName() {
        return ADDRESS_INITIALS;
    }

    public static String addressTypeColumnName() {
        return TYPE;
    }

    public static String getFullAddressGinIndexName() {
        return FULL_ADDRESS_GIN_INDEX;
    }

    public static String getAddressInitialsGinIndexName() {
        return ADDRESS_INITIALS_GIN_INDEX;
    }

}

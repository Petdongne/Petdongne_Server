CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- address 테이블 생성
CREATE TABLE IF NOT EXISTS address_test
(
    id               VARCHAR(255) PRIMARY KEY,
    full_address     VARCHAR(255),
    address_initials VARCHAR(255),
    type             VARCHAR(255)
);

-- GIN 인덱스 생성
CREATE INDEX IF NOT EXISTS gin_full_address_idx
    ON address_test
        USING gin (full_address gin_trgm_ops);

CREATE INDEX IF NOT EXISTS gin_address_initials_idx
    ON address_test
        USING gin (address_initials gin_trgm_ops);

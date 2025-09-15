CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS postgis;

create table legal_address (
    id bigint not null,
    code varchar(255),
    sido varchar(255),
    sigungu varchar(255),
    eupmyeondong varchar(255),
    li varchar(255),
    full_address varchar(255),
    address_initials varchar(255),
    center_point geometry(Point, 4326),
    polygon geometry(MultiPolygon, 4326),
    created_at timestamp(6),
    updated_at timestamp(6),
    primary key (id)
);


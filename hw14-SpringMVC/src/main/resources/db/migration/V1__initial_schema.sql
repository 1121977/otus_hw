-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);
 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 4 increment by 1;

create table client
(
    id bigint not null,
    primary key (id),
    addressid bigint,
    name varchar(50)
);

create table phoneData
(
    id bigint not null primary key,
    number varchar(50) not null,
    clientid bigint,
    constraint fk_client
        foreign key(clientid)
            references client(id)
);

create table addressdataset
(
    id bigint not null primary key,
    street varchar(50),
    CONSTRAINT fk_client_addressdataset FOREIGN KEY (id)
            REFERENCES public.client (id)
);

CREATE SEQUENCE public.id_generator
    INCREMENT 1
    START 4
    MINVALUE 4
    MAXVALUE 9223372036854775807
    CACHE 1;


-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);
 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 1;

create table client
(
    id bigint not null,
    primary key (id),
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


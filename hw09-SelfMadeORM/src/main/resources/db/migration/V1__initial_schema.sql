create table Client
(
    id bigserial not null primary key,
    name varchar(50),
    age int
);
create table Account
(
    no varchar(50) not null primary key,
    type varchar(50),
    rest float(10)
)
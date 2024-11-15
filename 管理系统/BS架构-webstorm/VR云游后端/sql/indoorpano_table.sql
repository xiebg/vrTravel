create table indoorpano_table
(
    id       bigint auto_increment
        primary key,
    addrname varchar(100) not null,
    pid      varchar(60)  not null
)DEFAULT CHARSET=utf8;


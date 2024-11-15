use login;
create table pano_table(
                           uid bigint auto_increment primary key not null ,
                           pano_name varchar(50) not null,
                           pano_addr varchar(100) not null,
                           pano_x double not null,
                           pano_y double not null,
                           pano_uid varchar(50) not null,
                           create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           pano_del bit default 0
)DEFAULT CHARSET=utf8;


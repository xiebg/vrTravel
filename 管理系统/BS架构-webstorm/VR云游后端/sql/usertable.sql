cteate database login;
CREATE TABLE user (
                              id bigint(20) NOT NULL AUTO_INCREMENT,
                              username varchar(30) NOT NULL,
                              password varchar(30) NOT NULL,
                              PRIMARY KEY (id)
);

insert into user(username,password) VALUES(admin,admin);

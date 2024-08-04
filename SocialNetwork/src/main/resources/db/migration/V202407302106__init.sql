CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50),
    lastname    VARCHAR(50),
    username    VARCHAR(50)  NOT NULL unique,
    password    VARCHAR(100) NOT NULL,
    birthdate   DATE,
    email       VARCHAR(100) unique,
    phonenumber VARCHAR(15) unique
);

CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) not null
);

CREATE TABLE users_roles
(
    user_id bigint not null,
    role_id int    not null,
    primary key (user_id, role_id),
    foreign key (user_id) references users (id),
    foreign key (role_id) references roles (id)
);
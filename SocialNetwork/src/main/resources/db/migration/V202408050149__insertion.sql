INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO users (name, lastname, username, password, birthdate, email, phonenumber)
VALUES ('user_name', 'user_lastname', 'user', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i',
        '2000-04-15', 'user@gmail.com', '+795435582412'),
       ('admin_name', 'admin_lastname', 'admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i',
        '2002-02-12', 'admin@gmail.com', '+795435581212');

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1),
       (2, 2);
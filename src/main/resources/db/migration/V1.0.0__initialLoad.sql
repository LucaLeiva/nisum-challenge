CREATE TABLE user_entity (
    id bigint IDENTITY(1,1) NOT NULL,
    email varchar(255) NOT NULL,
    name varchar(255) NULL,
    password varchar(255) NULL,
    created datetime2 NOT NULL,
    modified datetime2 NOT NULL,
    last_login datetime2 NOT NULL,
    token varchar(255) NULL,
    is_active bit NULL,

    CONSTRAINT PK_USER PRIMARY KEY (id)
);

CREATE TABLE phone (
    number varchar(255) NULL,
    city_code varchar(255) NULL,
    country_code varchar(255) NULL,
    user_id bigint NOT NULL,

    CONSTRAINT FK_PHONE_EMBEDDED FOREIGN KEY (user_id) REFERENCES user_entity (id)
);

-- usuario admin por defecto, contraseña adminadmin
INSERT INTO user_entity (email, name, password, created, modified, last_login, is_active)
    VALUES ('admin@hotmail.cl', 'admin', '$2a$10$RL2/CGe5P04ZgHOGzmDFBerU03Bo8XUQ5QqnaT4W0ysIvPmp.Y0XW', GETDATE(), GETDATE(), GETDATE(), 1);

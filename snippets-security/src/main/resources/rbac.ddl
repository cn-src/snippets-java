CREATE TABLE users
(
    id            bigserial PRIMARY KEY,
    name          varchar(50)        NOT NULL,
    email         varchar(50) UNIQUE NOT NULL,
    mobile        varchar(50) UNIQUE NOT NULL,
    password      varchar(500)       NOT NULL,
    enabled       boolean            NOT NULL,
    role_id       bigint,
    created_by_id bigint             NOT NULL,
    created_date  timestamp          NOT NULL
);
CREATE TABLE permission
(
    id            bigserial PRIMARY KEY,
    name          varchar(50)        NOT NULL,
    "group"       varchar(50)        NOT NULL,
    authority     varchar(50) UNIQUE NOT NULL,
    description   varchar(500)       NOT NULL,
    created_by_id bigint             NOT NULL,
    created_date  timestamp          NOT NULL
);
CREATE TABLE user_permission
(
    user_id       bigint NOT NULL,
    permission_id bigint NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    CONSTRAINT fk_user_permission_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_permission_permission_id FOREIGN KEY (permission_id) REFERENCES permission (id)
);

CREATE TABLE role
(
    id            bigserial PRIMARY KEY,
    name          varchar(50)  NOT NULL,
    description   varchar(500) NOT NULL,
    created_by_id bigint       NOT NULL,
    created_date  timestamp    NOT NULL
);

CREATE TABLE role_permission
(
    role_id       bigint    NOT NULL,
    permission_id bigint    NOT NULL,
    created_by_id bigint    NOT NULL,
    created_date  timestamp NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permission_role_id FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT fk_role_permission_permission_id FOREIGN KEY (permission_id) REFERENCES permission (id)
);
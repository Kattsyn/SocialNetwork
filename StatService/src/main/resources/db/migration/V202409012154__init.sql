CREATE TABLE authors
(
    id    BIGINT PRIMARY KEY,
    likes INT,
    views INT
);
CREATE TABLE posts
(
    id        BIGINT PRIMARY KEY,
    likes     INT,
    views     INT,
    author_id BIGINT,
    foreign key (author_id) references authors (id)
);
CREATE TYPE event_type as ENUM ('EVENT_LIKE', 'EVENT_VIEW');
CREATE TABLE events
(
    id BIGINT PRIMARY KEY,
    type event_type,
    timestamp timestamp without time zone,
    post_id BIGINT,
    user_id BIGINT,
    foreign key (post_id) references posts (id)
);

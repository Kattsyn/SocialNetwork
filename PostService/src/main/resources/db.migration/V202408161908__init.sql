CREATE TABLE posts
(
    post_id BIGSERIAL PRIMARY KEY,
    author_id BIGINT,
    header VARCHAR(255),
    text text
);
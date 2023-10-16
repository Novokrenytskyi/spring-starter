--liquibase formatted sql

--changeset novokren:1
ALTER TABLE users
    ADD COLUMN image VARCHAR(64);

--changeset novokren:2
ALTER TABLE users_aud
    ADD COLUMN image VARCHAR(64);


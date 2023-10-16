--liquibase formatted sql

--changeset novokren:1
ALTER TABLE users_aud
ALTER COLUMN username DROP NOT NULL;


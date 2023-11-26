-- liquibase formatted sql

-- changeset krakhimzyanov:1
CREATE TABLE notification_task
(
    p_key         SERIAL PRIMARY KEY,
    chat_id       BIGINT,
    notification  VARCHAR,
    date_time     TIMESTAMP
);

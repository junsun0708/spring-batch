DROP TABLE people1 IF EXISTS;

CREATE TABLE people1  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);

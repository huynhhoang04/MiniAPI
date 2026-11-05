CREATE TABLE company (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    website VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

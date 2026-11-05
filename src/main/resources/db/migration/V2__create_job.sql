CREATE TABLE job (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES company(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    salary_min NUMERIC(15,2),
    salary_max NUMERIC(15,2),
    location VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE INDEX idx_job_status ON job(status);
CREATE INDEX idx_job_location ON job(location);
CREATE INDEX idx_job_company_id ON job(company_id);

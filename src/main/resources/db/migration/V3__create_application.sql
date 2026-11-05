CREATE TABLE application (
    id BIGSERIAL PRIMARY KEY,
    job_id BIGINT NOT NULL REFERENCES job(id),
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    cv_url VARCHAR(500) NOT NULL,
    applied_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_application_job_email UNIQUE (job_id, email)
);
CREATE INDEX idx_application_job_id ON application(job_id);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Senha padrão: admin123 (BCrypt)
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$iGKWHVSY6slogRPGmTsebOB8m4jTiBQe45HdQ5H.mNegRadR9Ko72', 'ROLE_ADMIN'),
('user', '$2a$10$yLYyDUMv5pjAMcRqBAV4.OttibdDvYJB01AUXSeo2NltAEx.0U42e', 'ROLE_USER');
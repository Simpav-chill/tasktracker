CREATE TABLE project (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    status SMALLINT NOT NULL DEFAULT 0 CHECK (status BETWEEN 0 AND 1),
    created_at TIMESTAMP(6) NOT NULL DEFAULT now(),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT now()
);

CREATE TABLE task (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    status SMALLINT NOT NULL DEFAULT 0 CHECK (status BETWEEN 0 AND 2),
    priority SMALLINT NOT NULL DEFAULT 1 CHECK (priority BETWEEN 0 AND 2),
    created_at TIMESTAMP(6) NOT NULL DEFAULT now(),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT now(),
    project_id BIGINT REFERENCES project(id) ON DELETE CASCADE
);

CREATE INDEX idx_task_project_id ON task (project_id);
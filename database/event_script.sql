/* ================================
DATABASE
================================ */
CREATE DATABASE campus_events;

-- PostgreSQL
-- \c campus_events;
-- MySQL
-- USE campus_events;
/* ================================
USERS
================================ */
CREATE TABLE
    users (
        id BIGSERIAL PRIMARY KEY,
        full_name VARCHAR(100) NOT NULL,
        email VARCHAR(120) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'USER')),
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

/* ================================
EVENTS
================================ */
CREATE TABLE
    events (
        id BIGSERIAL PRIMARY KEY,
        title VARCHAR(150) NOT NULL,
        description TEXT,
        event_date TIMESTAMP NOT NULL,
        location VARCHAR(150),
        status VARCHAR(20) NOT NULL CHECK (status IN ('UPCOMING', 'LIVE', 'FINISHED')),
        created_by BIGINT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_event_creator FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE SET NULL
    );

/* ================================
POLLS
================================ */
CREATE TABLE
    polls (
        id BIGSERIAL PRIMARY KEY,
        event_id BIGINT NOT NULL,
        question VARCHAR(255) NOT NULL,
        is_open BOOLEAN DEFAULT TRUE,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_poll_event FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
    );

/* ================================
POLL OPTIONS
================================ */
CREATE TABLE
    poll_options (
        id BIGSERIAL PRIMARY KEY,
        poll_id BIGINT NOT NULL,
        option_text VARCHAR(200) NOT NULL,
        CONSTRAINT fk_option_poll FOREIGN KEY (poll_id) REFERENCES polls (id) ON DELETE CASCADE
    );

/* ================================
VOTES
================================ */
CREATE TABLE
    votes (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        poll_id BIGINT NOT NULL,
        option_id BIGINT NOT NULL,
        voted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_vote_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
        CONSTRAINT fk_vote_poll FOREIGN KEY (poll_id) REFERENCES polls (id) ON DELETE CASCADE,
        CONSTRAINT fk_vote_option FOREIGN KEY (option_id) REFERENCES poll_options (id) ON DELETE CASCADE,
        CONSTRAINT unique_user_poll UNIQUE (user_id, poll_id)
    );

/* ================================
INDEXES (PERFORMANCE)
================================ */
CREATE INDEX idx_event_status ON events (status);

CREATE INDEX idx_event_date ON events (event_date);

CREATE INDEX idx_poll_event ON polls (event_id);

CREATE INDEX idx_vote_poll ON votes (poll_id);

CREATE INDEX idx_vote_option ON votes (option_id);

/* ================================
SAMPLE DATA (OPTIONAL)
================================ */
-- Admin
INSERT INTO
    users (full_name, email, password, role)
VALUES
    (
        'Admin User',
        'admin@campus.com',
        'hashed_password',
        'ADMIN'
    );

-- Students
INSERT INTO
    users (full_name, email, password, role)
VALUES
    (
        'Ali Hassan',
        'ali@student.com',
        'hashed_password',
        'USER'
    ),
    (
        'Mona Ahmed',
        'mona@student.com',
        'hashed_password',
        'USER'
    );

-- Event
INSERT INTO
    events (
        title,
        description,
        event_date,
        location,
        status,
        created_by
    )
VALUES
    (
        'Tech Day 2026',
        'Annual technology event',
        '2026-03-10 10:00:00',
        'Main Hall',
        'LIVE',
        1
    );

-- Poll
INSERT INTO
    polls (event_id, question)
VALUES
    (1, 'Best Speaker?');

-- Poll Options
INSERT INTO
    poll_options (poll_id, option_text)
VALUES
    (1, 'Dr. Ahmed'),
    (1, 'Eng. Sara'),
    (1, 'Prof. Omar');

/* ================================
END OF SCRIPT
================================ */
-- =======================
-- USERS
-- =======================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =======================
-- ROLES
-- =======================
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO roles (name) VALUES
('super_admin'),
('activity_president'),
('web_manager'),
('activity_director'),
('committee_head'),
('committee_member'),
('member');


-- =======================
-- STUDENT ACTIVITIES
-- =======================
CREATE TABLE student_activities (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    description TEXT,
    category VARCHAR(150) NOT NULL,
    -- Each activity has ONE web manager
    president_id INTEGER UNIQUE REFERENCES users(id),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =======================
-- STUDENT ACTIVITY DIRECTORS (M:N)
-- =======================
CREATE TABLE activity_directors (
    activity_id INTEGER REFERENCES student_activities(id),
    activity_director_id INTEGER REFERENCES users(id),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(activity_id, activity_director_id)
);

-- =======================
-- COMMITTEES
-- =======================
CREATE TABLE committees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    stu_activity_id INTEGER NOT NULL REFERENCES student_activities(id),

    -- Director manages many committees (1:M)
    director_id INTEGER REFERENCES users(id),

    -- Committee Head (1:1)
    head_id INTEGER UNIQUE REFERENCES users(id),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(stu_activity_id, name)
);

-- =======================
-- COMMITTEE MEMBERS (M:N)
-- =======================
CREATE TABLE committee_members (
    committee_id INTEGER REFERENCES committees(id),
    committee_member INTEGER REFERENCES users(id),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(committee_id, committee_member)
);

-- =======================
-- EVENTS
-- =======================
CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,

    stu_activity_id INTEGER NOT NULL REFERENCES student_activities(id),
    committee_id INTEGER REFERENCES committees(id),

    start_at TIMESTAMP,
    end_at TIMESTAMP,

    created_by INTEGER REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =======================
-- POLLS
-- =======================
CREATE TABLE polls (
    id SERIAL PRIMARY KEY,
    event_id INTEGER NOT NULL REFERENCES events(id),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    is_public BOOLEAN DEFAULT TRUE,
    start_at TIMESTAMP,
    end_at TIMESTAMP,
    created_by INTEGER REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE poll_options (
    id SERIAL PRIMARY KEY,
    poll_id INTEGER NOT NULL REFERENCES polls(id),
    option_text VARCHAR(300) NOT NULL
);

CREATE TABLE votes (
    id SERIAL PRIMARY KEY,
    option_id INTEGER NOT NULL REFERENCES poll_options(id),
    user_id INTEGER NOT NULL REFERENCES users(id),
    voted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(option_id, user_id)
);

-- =======================
-- OTP & Refresh Token
-- =======================

CREATE TABLE refresh_tokens (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    token TEXT UNIQUE NOT NULL,
    expiry TIMESTAMP NOT NULL
);

CREATE TABLE otp_codes (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    code VARCHAR(6) NOT NULL,
    expiry TIMESTAMP NOT NULL,
    verified BOOLEAN DEFAULT FALSE
);


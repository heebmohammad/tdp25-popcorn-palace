
CREATE TABLE IF NOT EXISTS Movie (
    id SERIAL PRIMARY KEY,           -- BIGSERIAL for auto-generating IDs in PostgreSQL
    title VARCHAR(255) NOT NULL UNIQUE, -- Title should be unique
    genre VARCHAR(16) NOT NULL,         -- Genre column
    duration INT NOT NULL,              -- Duration column
    rating FLOAT NOT NULL CHECK (rating >= 0 AND rating <= 10),  -- Rating between 0 and 10
    release_year INT NOT NULL            -- Release year column
);

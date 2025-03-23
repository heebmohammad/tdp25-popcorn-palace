
CREATE TABLE IF NOT EXISTS Movie (
    id SERIAL PRIMARY KEY,           -- SERIAL for auto-generating IDs in PostgreSQL
    title VARCHAR(255) NOT NULL UNIQUE, -- Title should be unique
    genre VARCHAR(16) NOT NULL,         -- Genre column
    duration INT NOT NULL CHECK (duration > 0),              -- Duration column
    rating FLOAT NOT NULL CHECK (rating >= 0 AND rating <= 10),  -- Rating between 0 and 10
    release_year INT NOT NULL            -- Release year column
);

CREATE TABLE IF NOT EXISTS Showtime (
    id SERIAL PRIMARY KEY,           -- SERIAL for auto-generating IDs in PostgreSQL
    movie_id BIGINT NOT NULL,
    price FLOAT NOT NULL CHECK(price > 0),
    theater VARCHAR(255) NOT NULL,
    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ NOT NULL,
    CHECK (start_time < end_time),  -- Ensure start_time is before end_time
    FOREIGN KEY (movie_id) REFERENCES Movie(id) ON DELETE CASCADE  -- foreign key constraint
);

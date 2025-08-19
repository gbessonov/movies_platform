CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS movies_likes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    movie_id UUID NOT NULL,
    user_name VARCHAR(255) NOT NULL
    );

ALTER TABLE movies_likes
    ADD CONSTRAINT fk_movies_likes_movie_id
        FOREIGN KEY (movie_id) REFERENCES movies(id);

ALTER TABLE movies_likes
    ADD CONSTRAINT uk_movies_likes_user_movie
        UNIQUE (user_name, movie_id);

CREATE INDEX IF NOT EXISTS idx_movies_likes_user_id ON movies_likes (user_name);

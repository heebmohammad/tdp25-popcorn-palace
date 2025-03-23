
DELETE FROM Movie;
INSERT INTO Movie (title, genre, duration, rating, release_year)
VALUES 
    ('The Shawshank Redemption', 'DRAMA', 142, 9.3, 1994),
    ('The Dark Knight', 'ACTION', 152, 9.0, 2008),
    ('Forrest Gump', 'DRAMA', 142, 8.8, 1994),
    ('Inception', 'SCI_FI', 148, 8.8, 2010),
    ('The Matrix', 'SCI_FI', 136, 8.7, 1999);

DELETE FROM Showtime;
INSERT INTO Showtime (movie_id, price, theater, start_time, end_time)
VALUES 
    ((SELECT id FROM Movie WHERE title = 'The Shawshank Redemption'), 12, 'AMC Theater', '2025-02-14T11:47:46.125405Z', '2025-02-14T14:47:46.125405Z'),
    ((SELECT id FROM Movie WHERE title = 'The Dark Knight'), 18, 'Regal Cinema', '2025-02-14T12:00:00Z', '2025-02-14T14:30:00Z'),
    ((SELECT id FROM Movie WHERE title = 'Forrest Gump'), 15, 'Cinemark', '2025-02-15T09:30:00Z', '2025-02-15T12:00:00Z'),
    ((SELECT id FROM Movie WHERE title = 'Inception'), 14, 'Cineplex', '2025-02-15T13:00:00Z', '2025-02-15T15:30:00Z'),
    ((SELECT id FROM Movie WHERE title = 'The Matrix'), 16, 'AMC Theater', '2025-02-16T15:00:00Z', '2025-02-16T17:00:00Z'),
    ((SELECT id FROM Movie WHERE title = 'The Shawshank Redemption'), 12, 'Cinemark', '2025-02-16T18:00:00Z', '2025-02-16T20:30:00Z'),
    ((SELECT id FROM Movie WHERE title = 'The Dark Knight'), 18, 'Regal Cinema', '2025-02-17T20:00:00Z', '2025-02-17T22:30:00Z');

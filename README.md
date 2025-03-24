# Instructions

Content from [Instructions.md](Instructions.md).

## Overview of Endpoints

### Movies Endpoints
- **GET /movies/all**: Retrieves all movies with pagination.
- **POST /movies**: Creates a new movie (validates if a movie with the same title already exists).
- **POST /movies/update/{movieTitle}**: Updates an existing movie (ensures the movie exists and the new title is not taken).
- **DELETE /movies/{movieTitle}**: Deletes a movie by its title.

### Showtimes Endpoints
- **GET /showtimes/{showtimeId}**: Retrieves a showtime by its ID.
- **POST /showtimes**: Creates a new showtime.
- **POST /showtimes/update/{showtimeId}**: Updates an existing showtime.
- **DELETE /showtimes/{showtimeId}**: Deletes a showtime.

### Bookings Endpoints
- **POST /bookings**: Books a ticket.

---

## APIs

### Movies APIs

| API Description   | Endpoint                | Request Body                                                                 | Response Status | Response Body                                                                                                                                      |
|-------------------|-------------------------|------------------------------------------------------------------------------|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| Get all movies    | GET /movies/all         |                                                                              | 200 OK          | `[ { "id": 12345, "title": "Sample Movie Title 1", "genre": "Action", "duration": 120, "rating": 8.7, "releaseYear": 2025 }, ... ]`                 |
| Add a movie       | POST /movies            | `{ "title": "Sample Title", "genre": "Action", "duration": 120, "rating": 8.7, "releaseYear": 2025 }` | 200 OK          | `{ "id": 1, "title": "Sample Title", "genre": "Action", "duration": 120, "rating": 8.7, "releaseYear": 2025 }`                                     |
| Update a movie    | POST /movies/update/{movieTitle} | `{ "title": "New Title", "genre": "Action", "duration": 120, "rating": 8.7, "releaseYear": 2025 }` | 200 OK          |                                                                                                                                                |
| Delete a movie    | DELETE /movies/{movieTitle} |                                                                              | 200 OK          |                                                                                                                                                |

> **Valid Genres**: Action, Drama, Comedy, Thriller, Horror, Science Fiction, Romance, Documentary, Animation, Fantasy.

---

### Showtimes APIs

| API Description   | Endpoint                        | Request Body                                                                                             | Response Status | Response Body                                                                                                                                          |
|-------------------|---------------------------------|----------------------------------------------------------------------------------------------------------|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| Get showtime by ID| GET /showtimes/{showtimeId}     |                                                                                                          | 200 OK          | `{ "id": 1, "price": 50.2, "movieId": 1, "theater": "Sample Theater", "startTime": "...", "endTime": "..." }`                                         |
| Add a showtime    | POST /showtimes                 | `{ "movieId": 1, "price": 20.2, "theater": "Sample Theater", "startTime": "...", "endTime": "..." }`      | 200 OK          | `{ "id": 1, "price": 50.2, "movieId": 1, "theater": "Sample Theater", "startTime": "...", "endTime": "..." }`                                         |
| Update a showtime | POST /showtimes/update/{showtimeId} | `{ "movieId": 1, "price": 50.2, "theater": "Sample Theater", "startTime": "...", "endTime": "..." }`     | 200 OK          |                                                                                                                                                |
| Delete a showtime | DELETE /showtimes/{showtimeId}  |                                                                                                          | 200 OK          |                                                                                                                                                |

> **Date Format**: Ensure the date can be converted to `LocalDateTime`.

---

### Bookings APIs

| API Description   | Endpoint      | Request Body                                                | Response Status | Response Body                                                                                                                                          |
|-------------------|---------------|-------------------------------------------------------------|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| Book a ticket     | POST /bookings| `{ "showtimeId": 1, "seatNumber": 15, "userId": "UUID" }`   | 200 OK          | `{ "bookingId": "d1a6423b-4469-4b00-8c5f-e3cfc42eacae" }`                                                                                             |

> **Note**: `userId` must be a UUID.

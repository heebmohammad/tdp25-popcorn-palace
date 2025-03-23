package com.att.tdp.popcorn_palace.movie;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // find a movie by its title
    Optional<Movie> findByTitle(String title);

    // delete a movie by its title
    @Transactional
    void deleteByTitle(String title);

    // check if movie exists by its title
    boolean existsByTitle(String title);

    // update a movie by its title
    @Modifying
    @Transactional
    @Query("UPDATE Movie m SET m.title = :newTitle, m.genre = :newGenre, m.duration = :newDuration, m.rating = :newRating, m.releaseYear = :newReleaseYear WHERE m.title = :oldTitle")
    void updateMovieByTitle(String oldTitle, String newTitle, Genre newGenre, Integer newDuration, Float newRating, Integer newReleaseYear);
}

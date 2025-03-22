package com.att.tdp.popcorn_palace.movie;

import java.time.Year;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    // delete a movie by its title
    void deleteByTitle(String title);

    // update a movie by its title
    @Modifying
    @Transactional
    @Query("UPDATE Movie m SET m.title = :newTitle, m.genre = :newGenre, m.duration = :newDuration, m.rating = :newRating, m.releaseYear = :newReleaseYear WHERE m.title = :oldTitle")
    void updateMovieByTitle(String oldTitle, String newTitle, Genre newGenre, Integer newDuration, Float newRating, Year newReleaseYear);
}

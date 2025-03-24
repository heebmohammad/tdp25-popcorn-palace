package com.att.tdp.popcorn_palace.movie;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // find a movie by its title
    Optional<Movie> findByTitle(String title);

    // delete a movie by its title
    @Transactional
    void deleteByTitle(String title);

    // check if movie exists by its title
    boolean existsByTitle(String title);
}

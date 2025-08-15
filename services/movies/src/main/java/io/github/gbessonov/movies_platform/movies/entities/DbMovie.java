package io.github.gbessonov.movies_platform.movies.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.UUID;

@Entity
@Table(name="movies")
public class DbMovie {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public UUID id;

    @Column(nullable = false)
    public String title;

    @Formula("(SELECT COUNT(*) FROM movies_likes ml WHERE ml.movie_id = id)")
    public int numberOfLikes;
}

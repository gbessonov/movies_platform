package io.github.gbessonov.movies_platform.movies.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="movies")
public class DbMovie {
    @Id
    @GeneratedValue()
    public UUID id;

    @Column(nullable = false)
    public String title;
}

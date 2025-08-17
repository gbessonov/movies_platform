package io.github.gbessonov.movies_platform.users.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "users")
public class DbUser {
    @Id
    @GeneratedValue()
    public UUID id;

    @Column(nullable = false, unique = true)
    public String name;

    @Column(nullable = false)
    public String passwordHash;

    @Column(nullable = false)
    public String salt;

    @CreationTimestamp
    public Timestamp createdAt;

    @UpdateTimestamp
    public Timestamp updatedAt;
}

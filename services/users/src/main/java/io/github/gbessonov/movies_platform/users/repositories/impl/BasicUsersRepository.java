package io.github.gbessonov.movies_platform.users.repositories.impl;

import io.github.gbessonov.movies_platform.users.entities.DbUser;
import io.github.gbessonov.movies_platform.users.repositories.UsersRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BasicUsersRepository implements UsersRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final JpaUsersRepository db;

    @Autowired
    public BasicUsersRepository(JpaUsersRepository jpaUsersRepository) {
        this.db = jpaUsersRepository;
    }

    @Override
    public List<DbUser> getUsers() {
        return db.findAll();
    }

    @Override
    public Optional<DbUser> getUserById(UUID id) {
        return db.findById(id);
    }

    @Override
    public Optional<DbUser> getUserByName(String name) {
        String jpql = "SELECT u FROM DbUser u WHERE u.name = :name";

        TypedQuery<DbUser> query = entityManager.createQuery(jpql, DbUser.class);
        query.setParameter("name", name);

        List<DbUser> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public DbUser save(DbUser user) {
        return db.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        db.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        String jpql = "SELECT COUNT(u) > 0 FROM DbUser u WHERE u.name = :name";

        TypedQuery<Boolean> query = entityManager.createQuery(jpql, Boolean.class);
        query.setParameter("name", name);

        return query.getSingleResult();
    }
}

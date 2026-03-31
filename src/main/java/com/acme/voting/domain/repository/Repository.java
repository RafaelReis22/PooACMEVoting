package com.acme.voting.domain.repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Generic repository interface for persistence operations.
 *
 * @param <T> Entity type
 * @param <ID> ID type
 */
public interface Repository<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    Collection<T> findAll();
}

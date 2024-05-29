package com.pcare.pcare.utils;

import java.util.Collection;
import java.util.Optional;

/**
 * CRUD interface for describe simple operation over entity like: Create, Update, Delete, Find and Validate
 *
 * @param <ID> identifier class, like Primary Key in table
 * @param <T> entity type
 */
public interface CrudService<ID, T> {

    /**
     * retrieve entity by identifier
     *
     * @param id identifier of object
     * @return instance of found object
     */
    Optional<T> findById(ID id);

    Collection<T> findAll();

    T save(T object);

    T update(ID id, T object);

    void delete(ID id);

    void validate(T object);
}

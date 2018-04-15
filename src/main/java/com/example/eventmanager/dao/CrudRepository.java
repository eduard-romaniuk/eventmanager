package com.example.eventmanager.dao;

import java.util.stream.StreamSupport;

public interface CrudRepository<T> {

    void save(T entity);

    default void saveAll(Iterable<T> entities){
        entities.forEach(this::save);
    }

    T findOne(Long id);

    Iterable<T> findAll();

    void update(T entity);

    default void updateAll(Iterable<T> entities){
        entities.forEach(this::update);
    }

    void delete(T entity);

    default void deleteAll(Iterable<T> entities) {
        entities.forEach(this::delete);
    }

    default void deleteAll() {
        this.deleteAll(this.findAll());
    }

    default Long count() {
        return StreamSupport.stream(findAll().spliterator(), false).count();
    }

    default boolean exists(Long id) {
        return findOne(id) != null;
    }

}

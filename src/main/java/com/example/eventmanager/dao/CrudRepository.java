package com.example.eventmanager.dao;

public interface CrudRepository<T> {

    T findOne(Long id);

    Iterable<T> findAll();

    void save(T entity);

    default void saveAll(Iterable<T> entities){
        entities.forEach(this::save);
    }
}

package ru.otus.jdbc.mapper;

import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 * @param <T>
 */
public interface JdbcMapper<T> {
    Optional<?> insert(T objectData);

    void update(T objectData);

    void insertOrUpdate(T objectData);

    Optional<T> findById(Object id, Class<T> clazz);

    SessionManager getSessionManager();
//    List<T> findAll(Class<T> clazz);
}
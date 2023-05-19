package ru.otus.core.dao;

import ru.otus.core.model.Persistable;
import ru.otus.core.sessionmanager.SessionManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface DAO<T extends Persistable> {
    Optional<T> findById(long id);
    List<T> findAll();

    long insert(T t);

    void update(T t);

    long insertOrUpdate(T t);

    SessionManager getSessionManager();
}

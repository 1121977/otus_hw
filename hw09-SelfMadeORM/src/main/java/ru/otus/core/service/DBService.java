package ru.otus.core.service;

import java.util.Optional;

public interface DBService<T> {
    Optional<?> saveDataObject(T dataObject);

    Optional<T> getDataObject(long id);
    Optional<T> getDataObject(String id);
    Optional<T> getDataObject(Optional<?> id);

}

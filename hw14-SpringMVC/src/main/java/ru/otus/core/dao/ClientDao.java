package ru.otus.core.dao;

import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;

import java.util.List;
import java.util.Optional;


public interface ClientDao extends DAO<Client> {

    Optional<Client> findClientById(long id);
    List<Client> findClientsByNameRegExp(String name);
}
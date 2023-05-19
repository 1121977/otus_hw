package ru.otus.core.dao;

import ru.otus.core.model.Client;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;


public class ClientDaoImpl extends DAOImpl<Client> implements ClientDao {

    public ClientDaoImpl(SessionManagerHibernate sessionManager) {
        super(sessionManager, Client.class);
    }


}

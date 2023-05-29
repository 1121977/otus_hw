package ru.otus.core.dao;

import ru.otus.core.model.AddressDataSet;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

public class AddressDataSetDaoImpl extends DAOImpl<AddressDataSet> implements AddressDataSetDao{
    public AddressDataSetDaoImpl(SessionManagerHibernate sessionManager) {
        super(sessionManager, AddressDataSet.class);
    }
}

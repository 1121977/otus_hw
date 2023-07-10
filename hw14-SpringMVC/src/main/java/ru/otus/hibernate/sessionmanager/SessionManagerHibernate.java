package ru.otus.hibernate.sessionmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.core.sessionmanager.SessionManagerException;

public class SessionManagerHibernate implements SessionManager {

    private final SessionFactory sessionFactory;

    public SessionManagerHibernate(SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            throw new SessionManagerException("SessionFactory is null");
        }
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Session beginSession() {
        try {
            return sessionFactory.openSession();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

}
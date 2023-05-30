package ru.otus.core.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Persistable;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DAOImpl<T extends Persistable> implements DAO<T> {
    protected final SessionManagerHibernate sessionManager;
    protected final Class<T> entityClass;
    private static final Logger logger = LoggerFactory.getLogger(DAOImpl.class);

    protected DAOImpl(SessionManagerHibernate sessionManager, Class<T> entityClass) {
        this.sessionManager = sessionManager;
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> findById(long id) {
        sessionManager.beginSession();
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Optional<T> result = Optional.ofNullable(currentSession.getHibernateSession().find(entityClass, id));
            sessionManager.commitSession();
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            sessionManager.rollbackSession();
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        sessionManager.beginSession();
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Query query = currentSession.getHibernateSession().createQuery("select t from " + entityClass.getName() + " t");
            List<T> tList = query.getResultList();
            sessionManager.commitSession();
            return tList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            sessionManager.rollbackSession();
        }
        return new ArrayList<T>();
    }

    @Override
    public long insert(T t) {
        sessionManager.beginSession();
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(t);
            hibernateSession.flush();
            sessionManager.commitSession();
            return t.getId();
        } catch (Exception e) {
            sessionManager.rollbackSession();
            throw new ClientDaoException(e);
        }
    }

    @Override
    public void update(T t) {
        sessionManager.beginSession();
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(t);
            sessionManager.commitSession();
        } catch (Exception e) {
            sessionManager.rollbackSession();
            throw new ClientDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(T t) {
        sessionManager.beginSession();
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (t.getId() > 0) {
                hibernateSession.merge(t);
            } else {
                hibernateSession.persist(t);
                hibernateSession.flush();
            }
            sessionManager.commitSession();
            return t.getId();
        } catch (Exception e) {
            sessionManager.rollbackSession();
            throw new ClientDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public T delete(T t){
        sessionManager.beginSession();
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (t.getId() > 0) {
                hibernateSession.delete(t);
            }
            sessionManager.commitSession();
            return t;
        } catch (Exception e) {
            sessionManager.rollbackSession();
            throw new ClientDaoException(e);
        }
    }
}

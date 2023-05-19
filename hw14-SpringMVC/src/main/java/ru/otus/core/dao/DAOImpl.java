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

public abstract class DAOImpl<T extends Persistable> implements DAO<T>{
    protected final SessionManagerHibernate sessionManager;
    protected final Class<T> entityClass;
    private static final Logger logger = LoggerFactory.getLogger(DAOImpl.class);

    protected DAOImpl(SessionManagerHibernate sessionManager, Class<T> entityClass) {
        this.sessionManager = sessionManager;
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> findById(long id){

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(entityClass, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try{
            Query query = currentSession.getHibernateSession().createQuery("select t from " + entityClass.getName() + " t");
            List<T> tList = query.getResultList();
            return tList;
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return new ArrayList<T>();
    }

    @Override
    public long insert(T t) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(t);
            hibernateSession.flush();
            return t.getId();
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public void update(T t) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(t);
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(T t) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (t.getId() > 0) {
                hibernateSession.merge(t);
            } else {
                hibernateSession.persist(t);
                hibernateSession.flush();
            }
            return t.getId();
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }


    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}

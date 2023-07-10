package ru.otus.core.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Persistable;
import ru.otus.core.sessionmanager.SessionManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public abstract class DAOImpl<T extends Persistable> implements DAO<T> {
    protected final SessionManager sessionManager;
    protected final Class<T> entityClass;
    private static final Logger logger = LoggerFactory.getLogger(DAOImpl.class);

    protected DAOImpl(SessionManager sessionManager, Class<T> entityClass) {
        this.sessionManager = sessionManager;
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> findById(long id) {
        org.hibernate.Transaction tx = null;
        try (Session session = sessionManager.beginSession()){
            tx = session.beginTransaction();
            Optional<T> result = Optional.ofNullable(session.find(entityClass, id));
            tx.commit();
            return result;
        } catch (Exception e) {
            if(tx!=null) tx.rollback();
            logger.error(e.getMessage(), e);
            throw new ClientDaoException(e);
        }
    }

    @Override
    public List<T> findAll() {
        org.hibernate.Transaction tx = null;
        try (Session session = sessionManager.beginSession()){
            tx = session.beginTransaction();
            Query query = session.createQuery("select t from " + entityClass.getName() + " t");
            List<T> tList = query.getResultList();
            tx.commit();
            return tList;
        } catch (Exception e) {
            if(tx!=null) tx.rollback();
            logger.error(e.getMessage(), e);
            throw new ClientDaoException(e);
        }
    }

    @Override
    public long insert(T t) {
        org.hibernate.Transaction tx = null;
        try (Session session = sessionManager.beginSession()){
            tx = session.beginTransaction();
            session.persist(t);
            tx.commit();
            return t.getId();
        } catch (Exception e) {
            if(tx!=null) tx.rollback();
            logger.error(e.getMessage(), e);
            throw new ClientDaoException(e);
        }
    }

    @Override
    public void update(T t) {
        org.hibernate.Transaction tx = null;
        try (Session session = sessionManager.beginSession()){
            tx = session.beginTransaction();
//            session.merge(t);
            session.update(t);
            tx.commit();
        } catch (Exception e) {
            if(tx!=null) tx.rollback();
            logger.error(e.getMessage(), e);
            throw new ClientDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(T t) {
        org.hibernate.Transaction tx = null;
        try (Session session = sessionManager.beginSession()){
            tx = session.beginTransaction();
            if (t.getId() > 0) {
                session.merge(t);
            } else {
                session.persist(t);
            }
            tx.commit();
            return t.getId();
        } catch (Exception e) {
            tx.rollback();
            throw new ClientDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public T delete(T t) {
        org.hibernate.Transaction tx = null;
        try (Session session = sessionManager.beginSession()) {
            tx = session.beginTransaction();
            if (t.getId() > 0) {
                T _t = session.find(entityClass, t.getId());
                session.delete(_t);
            }
            tx.commit();
            return t;
        } catch (Exception e) {
            if(tx!=null) tx.rollback();
            logger.error(e.getMessage(), e);
            throw new ClientDaoException(e);
        }
    }
}

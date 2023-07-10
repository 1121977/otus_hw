package ru.otus.core.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Client;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDaoImpl extends DAOImpl<Client> implements ClientDao {

    private static final Logger logger = LoggerFactory.getLogger(ClientDaoImpl.class);
    public ClientDaoImpl(SessionManagerHibernate sessionManager) {
        super(sessionManager, Client.class);
    }

    @Override
    public Optional<Client> findClientById(long id) {
        org.hibernate.Transaction tx = null;
        Client result;
        try (Session session = sessionManager.beginSession()){
            tx = session.beginTransaction();
            result = session.find(entityClass, id);
            Hibernate.initialize(result.getPhoneDataSet());
            tx.commit();
        } catch (Exception e) {
            if(tx!=null) tx.rollback();
            logger.error(e.getMessage(), e);
            throw new ClientDaoException(e);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public List<Client> findClientsByNameRegExp(String name) {
        org.hibernate.Transaction tx = null;
        List<Client> resultSearch = new ArrayList<>();
        try (Session session = sessionManager.beginSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Client> cr = cb.createQuery(Client.class);
            Root<Client> root = cr.from(Client.class);
            cr.select(root).where(cb.like(root.get("name"), "%" + name + "%"));
            Query query = session.createQuery(cr);
            tx = session.beginTransaction();
            resultSearch = query.getResultList();
        } catch (Exception e){
            if(tx!=null) tx.rollback();
            logger.error(e.getMessage(), e);
            throw new ClientDaoException(e);
        }
        return resultSearch;
    }
}

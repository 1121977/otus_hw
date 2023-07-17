package ru.otus.core.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Client;
import ru.otus.core.model.Role;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public class RoleDaoImpl extends DAOImpl<Role> implements RoleDao{

    private static final Logger logger = LoggerFactory.getLogger(ClientDaoImpl.class);
    public RoleDaoImpl(SessionManager sessionManager) {
        super(sessionManager, Role.class);
    }

    @Override
    public Optional<Role> findById(long id){
        org.hibernate.Transaction tx = null;
        Role result;
        try (Session session = sessionManager.beginSession()){
            tx = session.beginTransaction();
            result = session.find(entityClass, id);
//            Hibernate.initialize(result.getPhoneDataSet());
            tx.commit();
        } catch (Exception e) {
            if(tx!=null) tx.rollback();
            logger.error(e.getMessage(), e);
            throw new RoleDaoException(e);
        }
        return Optional.ofNullable(result);
    }
}

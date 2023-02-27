package ru.otus.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.DbServiceDemo;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.dao.ClientDaoException;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.core.sessionmanager.DatabaseSession;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.model.User;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.otus.DbServiceDemo.HIBERNATE_CFG_FILE;

public class InHibernateClientUserDao implements UserDao {

    final private ClientDao clientDao;
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);
    static final protected byte MAX_CLIENTS = 7;

    public InHibernateClientUserDao() {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, PhoneData.class, AddressDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);

        clientDao = new ClientDaoHibernate(sessionManager);
        List<Client> clientsList = new ArrayList<>();
        for (int i = 0; i < MAX_CLIENTS; i++)
            clientsList.add(new Client("Client" + i));
        for (Client client : clientsList) {
            client.addPhoneData(new PhoneData("8-800-" + clientsList.indexOf(client)));
            client.setAddress(new AddressDataSet("Green Street, " + clientsList.indexOf(client), client));
        }

//        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            sessionManager.beginSession();
            long id;
            for (Client client : clientsList) {
                id = clientDao.insert(client);
            }
            sessionManager.commitSession();
        } catch (Exception e) {
            sessionManager.rollbackSession();
            throw new ClientDaoException(e);
        }


    }

    @Override
    public Optional<User> findById(long id) {
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = Optional.ofNullable(clientDao.findById(id).get().toUser());

                logger.info("client: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {

                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findRandomUser() {
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            Long countClients;
            try {
                DatabaseSessionHibernate databaseSessionHibernate = (DatabaseSessionHibernate) sessionManager.getCurrentSession();
                TypedQuery<Long> query = databaseSessionHibernate.getHibernateSession().createQuery("select count(cl) from Client cl", Long.class);
                countClients = query.getSingleResult();
                int randomClientNumber = 1 + (int)(Math.random()*MAX_CLIENTS);
                TypedQuery<Client> randomClientQuery = databaseSessionHibernate.getHibernateSession().createQuery("select cl from Client cl", Client.class).setFirstResult(randomClientNumber).setMaxResults(1);
                Client randomClient = randomClientQuery.getSingleResult();
                return Optional.ofNullable(randomClient.toUser());
            } catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
            sessionManager.commitSession();

        }
        return null;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.empty();
    }
}

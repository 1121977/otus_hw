package ru.otus.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.model.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class HibernateUserDao implements UserDao{
    final private SessionManagerHibernate sessionManager;

    public HibernateUserDao(Configuration configuration) {
        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class);
        sessionManager = new SessionManagerHibernate(sessionFactory);
    }

    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession;
        try {
            sessionManager.beginSession();
            currentSession = sessionManager.getCurrentSession();
            List<User> userList = currentSession.getHibernateSession().createQuery("select u from User u where u.id = :id ")
                    .setParameter("id",id).getResultList();
            sessionManager.close();
            return userList.stream().findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findRandomUser() {
        DatabaseSessionHibernate currentSession;
        try {
            sessionManager.beginSession();
            currentSession = sessionManager.getCurrentSession();
            List<User> userList = currentSession.getHibernateSession().createQuery("select u from User u").getResultList();
            sessionManager.close();
            Random random = new SecureRandom();
            return userList.stream().skip(random.nextInt(userList.size())).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        DatabaseSessionHibernate currentSession;
        try {
            sessionManager.beginSession();
            currentSession = sessionManager.getCurrentSession();
            List<User> userList = currentSession.getHibernateSession().createQuery("select u from User u").getResultList();
            sessionManager.close();
            return userList.stream().filter(uuu -> login.matches(uuu.getLogin())).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

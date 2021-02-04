package ru.otus.jdbc.sessionmanager;

import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.core.sessionmanager.SessionManagerException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SessionManagerJdbc implements SessionManager {

    private static final int TIMEOUT_IN_SECONDS = 5;
    private Connection connection;
    private DatabaseSessionJdbc databaseSession;
    private final String url;
    private final String user;
    private final String password;

    public SessionManagerJdbc(String url, String user, String password) {
        if (url == null) {
            throw new SessionManagerException("URL is null");
        }
        this.url = url.repeat(1);
        this.password = password.repeat(1);
        this.user = user.repeat(1);
    }

    @Override
    public void beginSession() {
        try {
            connection = DriverManager.getConnection(url,user,password);
            databaseSession = new DatabaseSessionJdbc(connection);
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void commitSession() {
        checkConnection();
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void rollbackSession() {
        checkConnection();
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void close() {
        checkConnection();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public DatabaseSessionJdbc getCurrentSession() {
        checkConnection();
        return databaseSession;
    }

    private void checkConnection() {
        try {
            if (connection == null || !connection.isValid(TIMEOUT_IN_SECONDS)) {
                throw new SessionManagerException("Connection is invalid");
            }
        } catch (SQLException ex) {
            throw new SessionManagerException(ex);
        }
    }
}
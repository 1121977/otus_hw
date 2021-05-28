package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Account;
import ru.otus.core.model.Client;
import ru.otus.core.service.DBService;
import ru.otus.core.service.DBServiceImpl;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.mapper.ClientDaoJdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/otusDB";
    private static final String USER = "user";
    private static final String PASSWORD = "pwd";

    public static void main(String[] args) {
// Общая часть
        flywayMigrations(URL, USER, PASSWORD);
        var sessionManager = new SessionManagerJdbc(URL, USER, PASSWORD);

// Работа с пользователем
        DbExecutorImpl<Client> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<Client> jdbcMapperClient = new JdbcMapperImpl<>(sessionManager, dbExecutor);
        ClientDao clientDao = new ClientDaoJdbcMapper(jdbcMapperClient);

// Код дальше должен остаться, т.е. clientDao должен использоваться
        var dbServiceClient = new DbServiceClientImpl(clientDao);
        var id = dbServiceClient.saveClient(new Client(0, "dbServiceClient", 20));
        Optional<Client> clientOptional = dbServiceClient.getClient(/*id*/0);

        clientOptional.ifPresentOrElse(
                client -> logger.info("created client, name:{}", client.getName()),
                () -> logger.info("client was not created")
        );
// Работа со счетом
        DbExecutorImpl<Account> dbExecutorAccount = new DbExecutorImpl<>();
        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<>(sessionManager, dbExecutorAccount);
        DBService<Account> dbServiceAccount = new DBServiceImpl<>(jdbcMapperAccount, Account.class);
        Optional<?> accountIDOptional = dbServiceAccount.saveDataObject(new Account("1qaz", "2wsx", 1.0F));
        Optional<Account> accountOptional = dbServiceAccount.getDataObject((String) accountIDOptional.get());
    }

    private static void flywayMigrations(String url, String user, String password) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(url, user, password)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
package ru.otus;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.core.service.DBServiceClient;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.*;

public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        List<Long> clientsId = new ArrayList<>();
        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        // Все главное см в тестах
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, PhoneData.class, AddressDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao userDao = new ClientDaoHibernate(sessionManager);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(userDao);

        Client vasia = new Client("Вася I");
        AddressDataSet address = new AddressDataSet("Lenina str.", vasia);
        vasia.addPhoneData(new PhoneData("+79175016148"));
        vasia.setAddress(address);
        long id = dbServiceClient.saveClient(vasia);
        for (int i=0; i< 10; i++) {
            vasia = new Client("Вася" + i);
            address = new AddressDataSet("Lenina str." + i , vasia);
            vasia.addPhoneData(new PhoneData("+79175016148" + i));
            vasia.setAddress(address);
            id = dbServiceClient.saveClient(vasia);
            clientsId.add(id);
        }
        for (int i=9; i>0; i--){
            id = clientsId.get(clientsId.size()-1);
            clientsId.remove(id);
            vasia = dbServiceClient.getClient(id).get();
            System.out.println(vasia.getName());
        }
//        long id = dbServiceClient.saveClient(new Client(0, "Вася"));
/*        Optional<Client> mayBeCreatedClient = dbServiceClient.getClient(id);
        mayBeCreatedClient.ifPresentOrElse((client) -> outputClient("Created client", client),
                () -> logger.info("Client not found"));

        id = dbServiceClient.saveClient(new Client(1L, "А! Нет. Это же совсем не Вася"));
        Optional<Client> mayBeUpdatedClient = dbServiceClient.getClient(id);
        mayBeUpdatedClient.ifPresentOrElse((client) -> outputClient("Updated client", client),
                () -> logger.info("Client not found"));*/
    }

    private static void outputClient(String header, Client client) {
        logger.info("-----------------------------------------------------------");
        logger.info(header);
        logger.info("client:{}", client);
    }
}

package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.core.service.DBServiceClient;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithBasicSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithBasicSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        // Все главное см в тестах
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, PhoneData.class, AddressDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao clientDao = new ClientDaoHibernate(sessionManager);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(clientDao);
        for (int i = 0; i<7; i++) {
            Client client = new Client("Ivan" + i);
            client.addPhoneData(new PhoneData("8800500500" + i));
            dbServiceClient.saveClient(client);
        }

        //LoginService loginService = new InMemoryLoginServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithBasicSecurity(WEB_SERVER_PORT,
                loginService, dbServiceClient, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}

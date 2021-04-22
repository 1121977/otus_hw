package ru.otus.jdbc.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с клиентами должно ")
class ClientDaoHibernateTest extends AbstractHibernateTest {

    private SessionManagerHibernate sessionManagerHibernate;
    private ClientDaoHibernate clientDaoHibernate;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        clientDaoHibernate = new ClientDaoHibernate(sessionManagerHibernate);
    }

    @Test
    @DisplayName(" корректно загружать клиента по заданному id")
    void shouldFindCorrectClientById() {
        Client expectedClient = new Client(0, "Вася");
        expectedClient.addPhoneData(new PhoneData("+7134567890"));
        saveClient(expectedClient);

        assertThat(expectedClient.getId()).isPositive();

        sessionManagerHibernate.beginSession();
        Optional<Client> mayBeClient = clientDaoHibernate.findById(expectedClient.getId());
        sessionManagerHibernate.commitSession();

        assertThat(mayBeClient).isPresent().get().isEqualToComparingFieldByField(expectedClient);
        assertThat(mayBeClient).isPresent().get().usingRecursiveComparison().isEqualTo(expectedClient);
    }

    @DisplayName(" корректно сохранять клиента")
    @Test
    void shouldCorrectSaveClient() {
        Client expectedClient = new Client(0, "Вася");
        expectedClient.addPhoneData(new PhoneData("+70987654321"));
        sessionManagerHibernate.beginSession();
        clientDaoHibernate.insertOrUpdate(expectedClient);
        long id = expectedClient.getId();
        sessionManagerHibernate.commitSession();

        assertThat(id).isGreaterThan(0);

        Client actualClient = loadClient(id);
        assertThat(actualClient).isNotNull().hasFieldOrPropertyWithValue("name", expectedClient.getName());

        expectedClient = new Client(id, "Не Вася");
        expectedClient.addPhoneData(new PhoneData("+71234567890"));
        sessionManagerHibernate.beginSession();
        clientDaoHibernate.insertOrUpdate(expectedClient);
        long newId = expectedClient.getId();
        sessionManagerHibernate.commitSession();

        assertThat(newId).isGreaterThan(0).isEqualTo(id);
        actualClient = loadClient(newId);
        assertThat(actualClient).isNotNull().hasFieldOrPropertyWithValue("name", expectedClient.getName());
//        assertThat(actualClient.getPhoneDataSet()).isNotNull().containsAll(expectedClient.getPhoneDataSet());
        Iterable<PhoneData> a = Arrays.asList(new PhoneData("+71234567890"));
        assertThat(actualClient.getPhoneDataSet()).isNotNull().containsAll(a);
    }

    @DisplayName(" возвращать менеджер сессий")
    @Test
    void getSessionManager() {
        assertThat(clientDaoHibernate.getSessionManager()).isNotNull().isEqualTo(sessionManagerHibernate);
    }
}
package ru.otus.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.core.service.DBServiceClient;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
class WithAbstractionsTest extends AbstractHibernateTest {

    private DBServiceClient dbServiceClient;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao clientDao = new ClientDaoHibernate(sessionManager);
        dbServiceClient = new DbServiceClientImpl(clientDao);
    }

    @Test
    @DisplayName(" корректно сохранять клиента")
    void shouldCorrectSaveClient() {
        Client savedClient = buildDefaultClient();
        PhoneData phoneDataHome = new PhoneData("+7(495)1324567");
        PhoneData phoneDataMobile = new PhoneData("+7(917)7654321");
        savedClient.addPhoneData(phoneDataHome);
        savedClient.addPhoneData(phoneDataMobile);
        AddressDataSet addressDataSet = new AddressDataSet("Lenin",savedClient);
        savedClient.setAddress(addressDataSet);
        long id = dbServiceClient.saveClient(savedClient);
        Client loadedClient = loadClient(id);

        assertThat(loadedClient).isNotNull().usingRecursiveComparison().isEqualTo(savedClient);

        System.out.println(savedClient);
        System.out.println(loadedClient);
    }

    @Test
    @DisplayName(" корректно загружать клиента")
    void shouldLoadCorrectClient() {
        Client savedClient = buildDefaultClient();
        PhoneData phoneDataHome = new PhoneData("+7(495)1324567");
        PhoneData phoneDataMobile = new PhoneData("+7(917)7654321");
        savedClient.addPhoneData(phoneDataHome);
        savedClient.addPhoneData(phoneDataMobile);
        AddressDataSet addressDataSet = new AddressDataSet("Lenin",savedClient);
        savedClient.setAddress(addressDataSet);

        saveClient(savedClient);

        Optional<Client> mayBeClient = dbServiceClient.getClient(savedClient.getId());

        assertThat(mayBeClient).isPresent().get().usingRecursiveComparison().isEqualTo(savedClient);

        System.out.println(savedClient);
        mayBeClient.ifPresent(System.out::println);
    }

    @Test
    @DisplayName(" корректно изменять ранее сохраненного клиента")
    void shouldCorrectUpdateSavedClient() {
        Client savedClient = buildDefaultClient();
        PhoneData phoneDataHome = new PhoneData("+7(495)1324567");
        savedClient.addPhoneData(phoneDataHome);
        saveClient(savedClient);

        Client savedClient2 = new Client(TEST_CLIENT_NEW_NAME);
        PhoneData phoneDataMobile = new PhoneData("+7(917)7654321");
        phoneDataHome = new PhoneData("+7(917)1234567");
        savedClient2.addPhoneData(phoneDataMobile);
        savedClient2.addPhoneData(phoneDataHome);
        AddressDataSet addressDataSet = new AddressDataSet("Lenin",savedClient2);
        savedClient2.setAddress(addressDataSet);

        long id = dbServiceClient.saveClient(savedClient2);
        Client loadedClient = loadClient(id);

        BiPredicate<Set<PhoneData>,Set<PhoneData>> biPredicate = (set1,set2) -> {
            if(set1.size()!=set2.size()){
                return false;
            }
            for (PhoneData phoneDataFromSet1:set1){
                boolean result=false;
                for (PhoneData phoneDataFromSet2:set2){
                    if (phoneDataFromSet1.equals(phoneDataFromSet2)){
                        result = true;
                        break;
                    }
                }
                if (!result){
                    return false;
                }
            }
            return true;
        };
        assertThat(loadedClient).isNotNull().usingRecursiveComparison().withEqualsForFields(biPredicate,"phoneDataSet").isEqualTo(savedClient2);

        System.out.println(savedClient);
        System.out.println(savedClient2);
        System.out.println(loadedClient);
    }

}
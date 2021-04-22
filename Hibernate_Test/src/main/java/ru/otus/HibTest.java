package ru.otus;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

public class HibTest {
    static public void main(String [] args){
        SessionFactory sessionFactory = new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build())
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(PhoneData.class)
                .buildMetadata().buildSessionFactory();
        Client client = new Client();
        PhoneData mobile = new PhoneData("+71231234567");
        client.addPhoneData(mobile);
        client.addPhoneData(new PhoneData("+73217654321"));
        client.setName("qaz");
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(client);
        tx.commit();
        ses
    }
}

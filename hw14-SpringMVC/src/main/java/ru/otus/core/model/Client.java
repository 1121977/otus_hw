package ru.otus.core.model;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "client")
public class Client implements Persistable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private AddressDataSet address;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "client",
            orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    Set<PhoneData> phoneDataSet = new HashSet<>();

    public Client() {
    }

    public Client(String name) {
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPhoneData(PhoneData phoneData) {
        phoneDataSet.add(phoneData);
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public Set<PhoneData> getPhoneDataSet() {
        return this.phoneDataSet;
    }

    public AddressDataSet getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "phone DataSet: " + phoneDataSet.toString() + '}';
    }

}
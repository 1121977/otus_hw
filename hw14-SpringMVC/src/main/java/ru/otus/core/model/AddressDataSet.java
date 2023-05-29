package ru.otus.core.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "AddressDataSet")
public class AddressDataSet implements Persistable{
    @Id
    @GeneratedValue(generator = "addressDataKeyGenerator")
    @GenericGenerator(
            name = "addressDataKeyGenerator",
            strategy = "foreign",
            parameters = {@Parameter(
                    name = "property",
                    value = "client"
            )}
    )
    private long id;
    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    private Client client;
    @Column(name = "street")
    private String street;
    public AddressDataSet(){
    }
    public AddressDataSet(String street){
        this.street = street;
    }
    public AddressDataSet(String street,Client client){
        this.street = street;
        this.client = client;
    }
    public void setClient(Client client){
        this.client = client;
    }

    public String getStreet(){
        return this.street;
    }

    public void setStreet(String street){
        this.street = street;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }
}

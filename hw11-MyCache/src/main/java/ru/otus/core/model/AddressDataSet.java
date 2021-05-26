package ru.otus.core.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "AddressDataSet")
public class AddressDataSet {
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
}

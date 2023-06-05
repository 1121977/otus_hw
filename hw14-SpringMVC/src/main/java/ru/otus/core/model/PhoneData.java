package ru.otus.core.model;

import javax.persistence.*;

@Entity
@Table(name = "phoneData")
public class PhoneData implements Persistable{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientID", nullable = false)
    private Client client;

    public PhoneData(){
        number = "default";
    }

    public PhoneData(String number){
        this.number = number;
    }

    public PhoneData(String number, Client client){
        this.client = client;
        this.number = number;
    }

    public PhoneData(Client client){
        this.client = client;
    }
    public String getNumber(){
        return number;
    }

    @Override
    public String toString(){
        return getNumber();
    }

    @Override
    public boolean equals(Object obj){
        if(obj==null){
            return false;
        }
        if(this==obj){
            return true;
        }
        if(obj instanceof PhoneData){
            return this.number.compareTo(((PhoneData) obj).getNumber())==0;
        }
        return false;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public Client getClient(){
        return this.client;
    }

    public void setClient(Client client){
        this.client = client;
    }
}

package ru.otus.core.model;

import javax.persistence.*;

@Entity
@Table(name = "phoneData")
public class PhoneData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @Column(name = "number")
    final private String number;

    public PhoneData(){
        number = "default";
    }

    public PhoneData(String number){
        this.number = number;
    }

    public String getNumber(){
        return number;
    }

    @Override
    public String toString(){
        return getNumber();
    }
}

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
}

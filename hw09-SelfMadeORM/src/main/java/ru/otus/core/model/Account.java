package ru.otus.core.model;

import ru.otus.core.dao.Id;

public class Account {
    @Id
    private String no;
    private String type;
    private float rest;

    public Account(){}
    public Account(String no, String type, float rest){
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    @Override
    public String toString(){
        return "Account{" +
                "no=" + this.no +
                ", type='" + type +
                ", rest =" + rest + '\'' +
                '}';
    }
}

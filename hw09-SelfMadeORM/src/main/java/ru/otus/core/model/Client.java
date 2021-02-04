package ru.otus.core.model;

import ru.otus.core.dao.Id;

public class Client {

    @Id
    private final long id;
    private final String name;
    private int age;

    public Client(){
        this.id=0;
        this.name="";
        this.age=0;
    }

    public Client(long id, String name, int age) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge(){
        return age;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name +
                ", age =" + age + '\'' +
                '}';
    }
}

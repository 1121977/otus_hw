package ru.otus;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyGsonTest {

    SomeClass someObject;
    SomeClass anotherObject;

    @BeforeEach
    void setUp() {
    }

    @Test
    void toJSONTest(){
        SomeClass.Builder someClassBuilder = new SomeClass.Builder();
        List<? super Object> list = new ArrayList<>();
        list.add(Boolean.TRUE);
        for (int i = 1; i < 4; i++) { list.add(Integer.toString(i)); }
        someObject = someClassBuilder
                .setA(5)
                .setB(list)
                .setC(new float[] {1F,2F,3F})
                .setD("Hello, Otus!")
                .setE(Math.E)
                .setF((short) 27)
                .build();
        Gson gson = new Gson();
        MyGson myGson = new MyGson();
        anotherObject = gson.fromJson(gson.toJson(someObject), SomeClass.class);
        assertEquals(someObject,anotherObject);
    }

}
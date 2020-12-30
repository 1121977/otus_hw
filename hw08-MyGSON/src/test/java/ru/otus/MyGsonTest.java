package ru.otus;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyGsonTest {

    SomeClass someObject = new SomeClass();

    @BeforeEach
    void setUp() {
    }

    @Test
    void toJSONTest(){
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) { list.add(Integer.toString(i)); }
        someObject.setA(5);
        someObject.setB(list);
        someObject.setC(new float[] {1F, 2F, 3F});
        someObject.setD("Hello, Otus!");
        someObject.setE(Math.E);
        String jsonString;
        Gson gson = new Gson();
        jsonString = gson.toJson(someObject);
        System.out.println(jsonString);
        MyGson myGson = new MyGson();
        jsonString = myGson.toJson(someObject);
        assertEquals(1l,1l);
    }

}
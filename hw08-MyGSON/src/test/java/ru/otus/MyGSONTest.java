package ru.otus;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyGSONTest {

    SomeClass someObject = new SomeClass();

    @BeforeEach
    void setUp() {
    }

    @Test
    void toJSONTest(){
        String jsonString;
        Gson gson = new Gson();
        jsonString = gson.toJson(someObject);
        System.out.println(jsonString);
        assertEquals(1l,1l);
    }

}
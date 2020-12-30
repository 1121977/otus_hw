package ru.otus;

import java.lang.reflect.Field;

public class MyGson {

    private String jSONString;

    String toJson(Object obj){
        Class<?> clazz = obj.getClass();
        Field [] fields = clazz.getDeclaredFields();
        jSONString = "aaa";
        return jSONString;
    }
}

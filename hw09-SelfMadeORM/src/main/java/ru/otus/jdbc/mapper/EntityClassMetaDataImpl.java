package ru.otus.jdbc.mapper;

import ru.otus.core.dao.Id;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData <T>{
    T objectData;

    public EntityClassMetaDataImpl(T objectData){
        this.objectData = objectData;
    }

    @Override
    public String getName() {
        return Arrays.stream(objectData.getClass().getName().toLowerCase().split("\\.")).reduce((first, second)->second).get();
    }

    @Override
    public Constructor<T> getConstructor() {
        return null;
    }

    @Override
    public Field getIdField() {
        for (Field field:objectData.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Id.class)){
                return field;
            }
        }
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        List<Field> fields = new ArrayList<>();
        fields.add(getIdField());
        fields.addAll(getFieldsWithoutId());
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> fieldsWithoutId = new ArrayList<>();
        try {
            for (Field field:objectData.getClass().getDeclaredFields()){
                if (!field.isAnnotationPresent(Id.class)){
                    fieldsWithoutId.add(field);
                }
            }
        }
        catch (Exception a){
            System.out.println(a.getMessage());
        }
        return fieldsWithoutId;
    }
}

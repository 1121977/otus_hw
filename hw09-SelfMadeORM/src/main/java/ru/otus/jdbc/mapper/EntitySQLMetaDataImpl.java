package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    Class<?> clazz;
    EntityClassMetaData entityClassMetaData;
    private static final Logger logger = LoggerFactory.getLogger(EntitySQLMetaDataImpl.class);

    EntitySQLMetaDataImpl(Class<?> clazz, EntityClassMetaData<?> entityClassMetaData){
        this.clazz = clazz;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return null;
    }

    @Override
    public String getSelectByIdSql() {
        String stringSelectByIdSql = "select ";
        List<Field> fieldsList = entityClassMetaData.getFieldsWithoutId();
        Iterator<Field> fieldListIterator = fieldsList.iterator();
        while (fieldListIterator.hasNext()){
            stringSelectByIdSql = stringSelectByIdSql + fieldListIterator.next().getName() + ", ";
        }
        stringSelectByIdSql = stringSelectByIdSql + entityClassMetaData.getIdField().getName() + " from "
        + entityClassMetaData.getName() + " where " + entityClassMetaData.getIdField().getName() + " = ?";
        return stringSelectByIdSql;
    }

    @Override
    public String getInsertSql() {
        String stringInsertSql = "insert into " + entityClassMetaData.getName() + " values (";
        Field idField = entityClassMetaData.getIdField();
        if (idField!=null){
            stringInsertSql = stringInsertSql + "?,";
        } else {
            logger.error("ID isn't defined.");
            throw new IllegalArgumentException("ID isn't defined.");
        }
        List<Field> fieldsList = entityClassMetaData.getFieldsWithoutId();
        Iterator<Field> fieldListIterator = fieldsList.iterator();
        while (fieldListIterator.hasNext()){
            stringInsertSql = stringInsertSql + "?";
            fieldListIterator.next();
            if(fieldListIterator.hasNext()){
                stringInsertSql = stringInsertSql + ",";
            } else {
                stringInsertSql = stringInsertSql + ")";
            }
        }
        return stringInsertSql;
}

    @Override
    public String getUpdateSql() {
        return null;
    }
}

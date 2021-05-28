package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.ClientDaoException;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static java.util.Optional.empty;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);
    protected final SessionManagerJdbc sessionManager;
    protected final DbExecutorImpl<T> dbExecutor;

    public JdbcMapperImpl(SessionManagerJdbc sessionManager, DbExecutorImpl<T> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Optional<?> insert(T objectData) {
        EntityClassMetaData<T> entityClassMetaData = new EntityClassMetaDataImpl<T>(objectData);
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl(objectData.getClass(), entityClassMetaData);
        List<Object> params = new ArrayList<>();
        Field idField = entityClassMetaData.getIdField();
        boolean initialFieldAccess = idField.canAccess(objectData);
        idField.setAccessible(true);
        try {
            params.add(idField.get(objectData));
            idField.setAccessible(initialFieldAccess);
            for (Field field: entityClassMetaData.getFieldsWithoutId()) {
                initialFieldAccess = field.canAccess(objectData);
                field.setAccessible(true);
                params.add(field.get(objectData));
                field.setAccessible(initialFieldAccess);
            }
            return dbExecutor.executeInsert(getConnection(), entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public void update(T objectData) {

    }

    @Override
    public void insertOrUpdate(T objectData) {
    }

    @Override
    public Optional<T> findById(Object id, Class<T> clazz) {
        EntityClassMetaData<T> entityClassMetaData = null;
        try {
            entityClassMetaData = new EntityClassMetaDataImpl<>(clazz.getConstructor(new Class[]{})
                    .newInstance(new Object[]{}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl(clazz, entityClassMetaData);
        try {
            return dbExecutor.executeSelect(getConnection(), entitySQLMetaData.getSelectByIdSql(),
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                Constructor<T> constructorT = clazz.getConstructor(new Class<?>[]{});
                                constructorT.setAccessible(true);
                                T resultT = constructorT.newInstance(new Object[]{});
                                for (Field field : clazz.getDeclaredFields()) {
                                    boolean access = field.canAccess(resultT);
                                    field.setAccessible(true);
                                    field.set(resultT, getValue(rs, field));
                                    field.setAccessible(access);
                                }
                                return resultT;
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return empty();
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Object getValue(ResultSet rs, Field field) throws SQLException {
        switch (field.getType().getName()) {
            case "long":
            case "Long":
                return rs.getLong(field.getName());
            case "java.lang.String":
                return rs.getString(field.getName());
            case "int":
                return rs.getInt(field.getName());
            case "float":
                return rs.getFloat(field.getName());
            default:
                return null;
        }
    }
}

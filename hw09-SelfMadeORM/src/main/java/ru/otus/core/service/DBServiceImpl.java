package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.mapper.JdbcMapper;
import java.util.Optional;

public class DBServiceImpl<T> implements DBService<T>{
    private static final Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);
    private final JdbcMapper<T> jdbcMapper;
    private final Class<T> clazz;

    public DBServiceImpl(JdbcMapper<T> jdbcMapper, Class<T> clazz) {
        this.jdbcMapper = jdbcMapper;
        this.clazz = clazz;
    }

    @Override
    public Optional<?> saveDataObject(T dataObject) {
        try (var sessionManager = jdbcMapper.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<?> id = jdbcMapper.insert(dataObject);
                sessionManager.commitSession();
                logger.info("created Client: {}", id);
                return id;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<T> getDataObject(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<T> getDataObject(String id) {
        return getDataObject(Optional.of(id));
    }

    @Override
    public Optional<T> getDataObject(Optional<?> id) {
        try (var sessionManager = jdbcMapper.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<T> dataOptional = jdbcMapper.findById(id.get(),clazz);
                logger.info("client: {}", dataOptional.orElse(null));
                return dataOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}

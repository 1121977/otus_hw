package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;
import ru.otus.core.sessionmanager.SessionManager;
import java.util.Optional;

public class ClientDaoJdbcMapper implements ClientDao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);
    private final JdbcMapper<Client> jdbcMapper;

    public ClientDaoJdbcMapper(JdbcMapper<Client> jdbcMapper) {
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Client> findById(long id) {
        return jdbcMapper.findById(id, Client.class);
    }

    @Override
    public long insert(Client client) {
        return (long) jdbcMapper.insert(client).get();
    }

    @Override
    public SessionManager getSessionManager() {
        return jdbcMapper.getSessionManager();
    }
}
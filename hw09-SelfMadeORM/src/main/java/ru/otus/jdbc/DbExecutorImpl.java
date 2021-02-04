package ru.otus.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbExecutorImpl<T> implements DbExecutor<T> {

    @Override
    public Optional<?> executeInsert(Connection connection, String sql, List<Object> params) throws SQLException {
        connection.setAutoCommit(false);
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return getValue(rs,params.get(0));
            }
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            throw ex;
        }
    }

    @Override
    public Optional<T> executeSelect(Connection connection, String sql, Object id,
                                     Function<ResultSet, T> rsHandler) throws SQLException {
        try (var pst = connection.prepareStatement(sql)) {
            pst.setObject(1, id);
            try (var rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    private Optional<?> getValue(ResultSet rs, final Object parameter) throws SQLException {
        switch (parameter.getClass().getName()) {
            case "java.lang.String":
                return Optional.of(rs.getString(1));
            case "java.lang.Long":
                return Optional.of(rs.getLong(1));
            default:
                return Optional.empty();
        }
    }
}
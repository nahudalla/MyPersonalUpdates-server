package com.mypersonalupdates.db.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Esta clase convierte el resultado de una consulta en la base de datos a
 * una instancia de la clase {@link Instant}
 */
public final class InstantMapper implements ResultSetMapper<Instant> {
    @Override
    public Instant map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp("timestamp");
        return Instant.ofEpochMilli(timestamp.getTime());
    }
}

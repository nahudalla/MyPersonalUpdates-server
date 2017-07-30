package com.mypersonalupdates.db.mappers;

import com.mypersonalupdates.db.DBException;
import com.mypersonalupdates.users.Category;
import com.mypersonalupdates.users.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta clase convierte los resultados de una consulta en la base de datos
 * en una instancia de la clase {@link Category}
 */
public final class CategoryMapper implements ResultSetMapper<Category> {
    @Override
    public Category map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Long userID = resultSet.getLong("userID");
        String name = resultSet.getString("name");
        try {
            return Category.create(
                    User.getInstanceFromExistingID(userID),
                    name
            );
        } catch (DBException e) {
            e.printStackTrace();
            // TODO: log
            return null;
        }
    }
}

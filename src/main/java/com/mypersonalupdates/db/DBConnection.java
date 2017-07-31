package com.mypersonalupdates.db;

import com.mypersonalupdates.Config;
import org.mariadb.jdbc.MySQLDataSource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.sql.DataSource;

/**
 * Esta clase encapsula la conexción a la base de datos,
 * que es realizada con la librería JDBI.
 */
public final class DBConnection {
    private static final String DATABASE_URL = Config.get().getString("db.url");
    private static final String DATABASE_USER = Config.get().getString("db.user");
    private static final String DATABASE_PASSWORD = Config.get().getString("db.password");

    private static DataSource getDataSource() {
        MySQLDataSource mysqlDS = new MySQLDataSource();

        mysqlDS.setURL(DBConnection.DATABASE_URL);
        mysqlDS.setUser(DBConnection.DATABASE_USER);
        mysqlDS.setPassword(DBConnection.DATABASE_PASSWORD);

        return mysqlDS;
    }

    public static DBConnection getInstance() {
        return new DBConnection();
    }

    private final DBI dbi;

    private DBConnection() {
        this.dbi = new DBI(DBConnection.getDataSource());
    }

    public <T> T onDemand(Class<T> objType) {
        return this.dbi.onDemand(objType);
    }

    public <T> T withHandle(HandleCallback<T> handleCallback) {
        return this.dbi.withHandle(handleCallback);
    }

    public interface HandleCallback<T> extends org.skife.jdbi.v2.tweak.HandleCallback<T>{
        T withHandle(Handle handle) throws Exception;
    }
}

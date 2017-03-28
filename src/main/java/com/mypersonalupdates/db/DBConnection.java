package com.mypersonalupdates.db;

import org.mariadb.jdbc.MySQLDataSource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.sql.DataSource;

public class DBConnection {
    private static String DATABASE_URL = "jdbc:mysql://localhost:3306/mypersonalupdates";
    private static String DATABASE_USER = "mypersonalupdatesuser";
    private static String DATABASE_PASSWORD = "58fa510222a612101bf6e36a9ae696b987d876b75";

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

    private DBI dbi;

    private DBConnection() {
        this.dbi = new DBI(DBConnection.getDataSource());
    }

    public <T> T withHandle(HandleCallback<T> handleCallback) throws Exception {
        return this.dbi.withHandle(handleCallback);
    }

    public interface HandleCallback<T> extends org.skife.jdbi.v2.tweak.HandleCallback<T>{
        T withHandle(Handle handle) throws Exception;
    }
}

package org.example;

import java.sql.Connection;

public abstract class DAOFactory {
    public static final int MYSQL_JDBC = 1;

    public abstract Connection getInstance();

    public static DAOFactory getDaoFactory(int db){
        switch (db){
            case MYSQL_JDBC : return new MysqlFactory();
            default: return null;
        }
    }

}

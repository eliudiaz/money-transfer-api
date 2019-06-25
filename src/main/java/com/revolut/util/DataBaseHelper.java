package com.revolut.util;

import org.apache.commons.dbcp.BasicDataSource;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Singleton
public final class DataBaseHelper {

    private Properties configurations;
    private BasicDataSource dataSource;

    private void loadConfiguration() {
        if (configurations == null) {
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            configurations = new Properties();
            dataSource = new BasicDataSource();
            try (final InputStream stream = loader.getResourceAsStream("app.properties")) {
                configurations.load(stream);
                dataSource.setUrl(configurations.getProperty("db.url"));
                dataSource.setUsername(configurations.getProperty("db.username"));
                dataSource.setMinIdle(1);
                dataSource.setMaxIdle(2);
                dataSource.setMaxOpenPreparedStatements(100);
                System.out.println("Driver ready!");
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                throw new RuntimeException("Not able to load application configurations!");
            }
        }
    }

    public Connection getConnection() throws SQLException {
        this.loadConfiguration();
        return dataSource.getConnection();
    }


}

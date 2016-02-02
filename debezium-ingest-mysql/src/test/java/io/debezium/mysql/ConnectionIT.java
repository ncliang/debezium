/*
 * Copyright Debezium Authors.
 * 
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;

public class ConnectionIT {

    @Ignore
    @Test
    public void shouldConnectToDefaulDatabase() throws SQLException {
        try (MySQLConnection conn = MySQLConnection.forTestDatabase("mysql");) {
            conn.connect();
        }
    }

    @Test
    public void shouldDoStuffWithDatabase() throws SQLException {
        try (MySQLConnection conn = MySQLConnection.forTestDatabase("readbinlog_test");) {
            conn.connect();
            // Set up the table as one transaction and wait to see the events ...
            conn.execute("DROP TABLE IF EXISTS person",
                         "CREATE TABLE person ("
                                 + "  name VARCHAR(255) primary key,"
                                 + "  birthdate DATE NULL,"
                                 + "  age INTEGER NULL DEFAULT 10,"
                                 + "  salary DECIMAL(5,2),"
                                 + "  bitStr BIT(18)"
                                 + ")");
            conn.execute("SELECT * FROM person");
            try (ResultSet rs = conn.connection().getMetaData().getColumns("readbinlog_test", null, null, null)) {
                conn.print(rs);
            }
        }
    }

    @Ignore
    @Test
    public void shouldConnectToEmptyDatabase() throws SQLException {
        try (MySQLConnection conn = MySQLConnection.forTestDatabase("emptydb");) {
            conn.connect();
        }
    }
}
package com.github.jacekpoz;

import com.github.jacekpoz.common.DatabaseConnector;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseConnectorTest {

    @Test
    public void shouldConnectToDatabase() {
        try {
            new DatabaseConnector(
                    "jdbc:mysql://localhost:3306/mydatabase",
                    "chat-client", "DB_Password_0123456789"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Couldn't connect to database");
        }
    }


}

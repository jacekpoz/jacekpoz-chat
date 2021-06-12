package com.github.jacekpoz.common;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {

    public enum RegisterResult {
        USERNAME_TAKEN,
        ACCOUNT_CREATED,
        SQL_EXCEPTION
    }

    public enum LoginResult {
        LOGGED_IN,
        ACCOUNT_DOESNT_EXIST,
        WRONG_PASSWORD,
        SQL_EXCEPTION
    }

    private final Connection con;

    public DatabaseConnector(String url, String dbUsername, String dbPassword) throws SQLException {
        con = DriverManager.getConnection(url, dbUsername, dbPassword);
    }

    public RegisterResult register(String username, char[] password) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT Username FROM " + GlobalStuff.USERS_TABLE_NAME + " WHERE Username = '" + username + "';");
            if (rs.next()) return RegisterResult.USERNAME_TAKEN;

            Argon2 argon2 = Argon2Factory.create();
            String hash = argon2.hash(10, 65536, 1, password);
            argon2.wipeArray(password);

            st.execute("INSERT INTO users (Username, HashedPassword, Host, Port) " +
                    "VALUES ('" + username + "', '" + hash + "', '" + Util.getPublicIP() + "', " + GlobalStuff.SERVER_PORT + ");");

            return RegisterResult.ACCOUNT_CREATED;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return RegisterResult.SQL_EXCEPTION;
        }
    }

    public LoginResult login(String username, char[] password) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.USERS_TABLE_NAME + " WHERE Username = '" + username + "';");
            if (!rs.next()) return LoginResult.ACCOUNT_DOESNT_EXIST;

            Argon2 argon2 = Argon2Factory.create();
            String dbHash = rs.getString("HashedPassword");

            if (argon2.verify(dbHash, password)) {
                argon2.wipeArray(password);
                updateHostAndPort(rs.getLong("ID"));
                return LoginResult.LOGGED_IN;
            }

            return LoginResult.WRONG_PASSWORD;
        } catch (SQLException e) {
            e.printStackTrace();
            return LoginResult.SQL_EXCEPTION;
        }
    }

    private void updateHostAndPort(long id) {
        try (Statement st = con.createStatement()) {
            st.execute("UPDATE " + GlobalStuff.USERS_TABLE_NAME + "\n" +
                    "SET Host = '" + Util.getPublicIP() + "', Port = " + GlobalStuff.SERVER_PORT + "\n" +
                    " WHERE ID = " + id + ";");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUserInfo(long id) {
        return getUserInfo0(String.valueOf(id), "ID");
    }

    public UserInfo getUserInfo(String name) {
        return getUserInfo0(name, "Username");
    }

    private UserInfo getUserInfo0(String arg, String argName) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.USERS_TABLE_NAME + " WHERE " + argName + " = '" + arg + "';");
            if (!rs.next()) return null;

            long id = rs.getLong("ID");
            String nickname = rs.getString("Username");
            String hashedPassword = rs.getString("HashedPassword");

            return new UserInfo(id, nickname, hashedPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<UserInfo> getAllUsers() {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.USERS_TABLE_NAME + ";");
            List<UserInfo> allUsers = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("ID");
                String username = rs.getString("Username");
                String hashedPassword = rs.getString("HashedPassword");
                allUsers.add(new UserInfo(id, username, hashedPassword));
            }

            return allUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addFriend(UserInfo user, UserInfo userToAdd) {

    }
}

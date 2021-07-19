package com.github.jacekpoz.common;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
****************************
* template for each method *
****************************

try (Statement st = con.createStatement()) {

    } catch (SQLException e) {
        e.printStackTrace();
    }

 */

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

    public enum AddFriendResult {
        ADDED_FRIEND,
        ALREADY_FRIEND,
        SAME_USER,
        SQL_EXCEPTION
    }

    public enum RemoveFriendResult {
        REMOVED_FRIEND,
        SAME_USER,
        SQL_EXCEPTION
    }

    private final Connection con;

    public DatabaseConnector(String url, String dbUsername, String dbPassword) throws SQLException {
        con = DriverManager.getConnection(url, dbUsername, dbPassword);
    }

    public RegisterResult register(String username, char[] password) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT username FROM " + GlobalStuff.USERS_TABLE + " WHERE username = '" + username + "'");
            if (rs.next()) return RegisterResult.USERNAME_TAKEN;

            Argon2 argon2 = Argon2Factory.create();
            String hash = argon2.hash(10, 65536, 1, password);
            argon2.wipeArray(password);

            st.execute("INSERT INTO " + GlobalStuff.USERS_TABLE + "(username, password_hash)" +
                    " VALUES ('" + username + "', '" + hash + "')");

            return RegisterResult.ACCOUNT_CREATED;
        } catch (SQLException e) {
            e.printStackTrace();
            return RegisterResult.SQL_EXCEPTION;
        }
    }

    public LoginResult login(String username, char[] password) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.USERS_TABLE + " WHERE username = '" + username + "'");
            if (!rs.next()) return LoginResult.ACCOUNT_DOESNT_EXIST;

            Argon2 argon2 = Argon2Factory.create();
            String dbHash = rs.getString("password_hash");

            if (argon2.verify(dbHash, password)) {
                argon2.wipeArray(password);
                return LoginResult.LOGGED_IN;
            }

            return LoginResult.WRONG_PASSWORD;
        } catch (SQLException e) {
            e.printStackTrace();
            return LoginResult.SQL_EXCEPTION;
        }
    }

    public AddFriendResult addFriend(UserInfo user, UserInfo friend) {
        if (user.getId() == friend.getId()) return AddFriendResult.SAME_USER;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.FRIENDS_TABLE +
                    " WHERE user_id = " + user.getId() + " AND friend_id = " + friend.getId());
            if (rs.next()) return AddFriendResult.ALREADY_FRIEND;

            st.execute("INSERT INTO " + GlobalStuff.FRIENDS_TABLE +
                    " VALUES (" + user.getId() + ", " + friend.getId() + ")");

            return AddFriendResult.ADDED_FRIEND;
        } catch (SQLException e) {
            e.printStackTrace();
            return AddFriendResult.SQL_EXCEPTION;
        }
    }

    public RemoveFriendResult removeFriend(UserInfo user, UserInfo friend) {
        if (user.getId() == friend.getId()) return RemoveFriendResult.SAME_USER;

        try (Statement st = con.createStatement()) {
            st.execute("DELETE FROM " + GlobalStuff.FRIENDS_TABLE +
                    " WHERE user_id = " + user.getId() + " AND friend_id = " + friend.getId());

            return RemoveFriendResult.REMOVED_FRIEND;
        } catch (SQLException e) {
            e.printStackTrace();
            return RemoveFriendResult.SQL_EXCEPTION;
        }
    }

    public void addMessage(Message m) {
        try (Statement st = con.createStatement()) {
            st.execute("INSERT INTO " + GlobalStuff.MESSAGES_TABLE +
                    "(" + m.getMessageID() + ", " + m.getChatID() + ", " + m.getAuthorID() + ", " +
                    "'" + m.getContent() + "', " + m.getSendDate() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUser(long id) {
        return getUser0(String.valueOf(id), "user_id");
    }

    public UserInfo getUser(String name) {
        return getUser0(name, "username");
    }

    private UserInfo getUser0(String arg, String argName) {

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.USERS_TABLE + " WHERE " + argName + " = '" + arg + "'");
            if (!rs.next()) return null;

            long id = rs.getLong("user_id");
            String nickname = rs.getString("username");
            String hashedPassword = rs.getString("password_hash");
            Timestamp joined = rs.getTimestamp("date_joined");

            UserInfo returned = new UserInfo(id, nickname, hashedPassword, joined);

            rs = st.executeQuery("SELECT friend_id FROM " + GlobalStuff.FRIENDS_TABLE + " WHERE user_id = " + id);

            while (rs.next()) returned.addFriend(rs.getLong("friend_id"));

            return returned;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<UserInfo> getAllUsers() {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.USERS_TABLE);
            List<UserInfo> allUsers = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("user_id");
                allUsers.add(getUser(id));
            }

            return allUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Chat> getAllChats() {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT chat_id FROM " + GlobalStuff.CHATS_TABLE);
            List<Chat> allChats = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("chat_id");
                allChats.add(getChat(id));
            }
            return allChats;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Chat getChat(long chatID) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.CHATS_TABLE +
                    " WHERE chat_id = " + chatID);
            if (!rs.next()) return null;
            String name = rs.getString("name");
            Timestamp created = rs.getTimestamp("date_created");

            Chat c = new Chat(chatID, name, created, getChatMessageCounter(chatID));

            getMessagesFromChat(chatID)
                    .forEach(message -> c.getMessageIDs().add(message.getMessageID()));

            getUsersInChat(chatID)
                    .forEach(user -> c.getMembers().add(user));

            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message getMessage(long messageID, long chatID) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + GlobalStuff.MESSAGES_TABLE +
                    " WHERE message_id = " + messageID + " AND chat_id = " + chatID);
            if (!rs.next()) return new Message(String.format("Message{messageID=%s,chatID=%s} doesn't exist",
                    messageID, chatID));

            long authorID = rs.getLong("author_id");
            String content = rs.getString("content");
            Timestamp sendDate = rs.getTimestamp("date_sent");

            return new Message(messageID, chatID, authorID, content, sendDate);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message("getMessage() failed");
        }
    }

    public long getChatMessageCounter(long chatID) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT message_counter FROM " + GlobalStuff.CHATS_MESSAGE_COUNTERS_TABLE +
                    " WHERE chat_id = " + chatID);
            if (!rs.next()) return -1;

            return rs.getLong("message_counter");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Message> getMessagesFromChat(long chatID) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT message_id FROM " + GlobalStuff.MESSAGES_TABLE +
                    " WHERE chat_id = " + chatID);
            List<Message> messages = new ArrayList<>();

            while (rs.next()) {
                long messageID = rs.getLong("message_id");
                messages.add(getMessage(messageID, chatID));
            }

            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<UserInfo> getUsersInChat(long chatID) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT user_id FROM " + GlobalStuff.USERS_IN_CHATS_TABLE +
                    " WHERE chat_id = " + chatID);
            List<UserInfo> users = new ArrayList<>();

            while (rs.next()) {
                long userID = rs.getLong("user_id");
                users.add(getUser(userID));
            }

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Chat> getUsersChats(long userID) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT chat_id FROM " + GlobalStuff.USERS_IN_CHATS_TABLE +
                    " WHERE user_id = " + userID);
            List<Chat> chats = new ArrayList<>();

            while (rs.next()) {
                long chatID = rs.getLong("chat_id");
                chats.add(getChat(chatID));
            }

            return chats;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

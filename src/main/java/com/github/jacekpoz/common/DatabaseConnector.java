package com.github.jacekpoz.common;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    private final Connection con;

    public DatabaseConnector(String url, String dbUsername, String dbPassword) throws SQLException {
        con = DriverManager.getConnection(url, dbUsername, dbPassword);
    }

    public enum RegisterResult {
        USERNAME_TAKEN,
        ACCOUNT_CREATED,
        SQL_EXCEPTION
    }

    public RegisterResult register(String username, char[] password) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT username FROM " + Constants.USERS_TABLE + " WHERE username = '" + username + "'");
            if (rs.next()) return RegisterResult.USERNAME_TAKEN;

            Argon2 argon2 = Argon2Factory.create();
            String hash = argon2.hash(10, 65536, 1, password);
            argon2.wipeArray(password);

            st.execute("INSERT INTO " + Constants.USERS_TABLE + "(username, password_hash)" +
                    " VALUES ('" + username + "', '" + hash + "')");

            return RegisterResult.ACCOUNT_CREATED;
        } catch (SQLException e) {
            e.printStackTrace();
            return RegisterResult.SQL_EXCEPTION;
        }
    }

    public enum LoginResult {
        LOGGED_IN,
        ACCOUNT_DOESNT_EXIST,
        WRONG_PASSWORD,
        SQL_EXCEPTION
    }

    public LoginResult login(String username, char[] password) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.USERS_TABLE + " WHERE username = '" + username + "'");
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

    public enum AddFriendResult {
        ADDED_FRIEND,
        ALREADY_FRIEND,
        SAME_USER,
        SQL_EXCEPTION
    }

    public AddFriendResult addFriend(User user, User friend) {
        if (user.getId() == friend.getId()) return AddFriendResult.SAME_USER;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.FRIENDS_TABLE +
                    " WHERE user_id = " + user.getId() + " AND friend_id = " + friend.getId());
            if (rs.next()) return AddFriendResult.ALREADY_FRIEND;

            st.execute("INSERT INTO " + Constants.FRIENDS_TABLE +
                    " VALUES (" + user.getId() + ", " + friend.getId() + ")");

            return AddFriendResult.ADDED_FRIEND;
        } catch (SQLException e) {
            e.printStackTrace();
            return AddFriendResult.SQL_EXCEPTION;
        }
    }

    public enum RemoveFriendResult {
        REMOVED_FRIEND,
        SAME_USER,
        SQL_EXCEPTION
    }

    public RemoveFriendResult removeFriend(User user, User friend) {
        if (user.getId() == friend.getId()) return RemoveFriendResult.SAME_USER;

        try (Statement st = con.createStatement()) {
            st.execute("DELETE FROM " + Constants.FRIENDS_TABLE +
                    " WHERE user_id = " + user.getId() + " AND friend_id = " + friend.getId());

            return RemoveFriendResult.REMOVED_FRIEND;
        } catch (SQLException e) {
            e.printStackTrace();
            return RemoveFriendResult.SQL_EXCEPTION;
        }
    }

    public void addMessage(Message m) {
        try (Statement st = con.createStatement()) {
            st.execute("INSERT INTO " + Constants.MESSAGES_TABLE +
                    "(message_id, chat_id, author_id, content)" +
                    "VALUES (" + m.getMessageID() + ", " + m.getChatID() + ", " +
                    m.getAuthorID() + ", " + "'" + m.getContent() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public enum SendFriendRequestResult {
        SENT_SUCCESSFULLY,
        ALREADY_SENT,
        ALREADY_FRIENDS,
        SQL_EXCEPTION
    }

    public SendFriendRequestResult sendFriendRequest(User sender, User friend) {
        if (isFriend(sender, friend)) return SendFriendRequestResult.ALREADY_FRIENDS;

        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE sender_id = " + sender.getId() + " AND friend_id = " + friend.getId());
            if (rs.next()) return SendFriendRequestResult.ALREADY_SENT;

            st.execute("INSERT INTO " + Constants.FRIEND_REQUESTS_TABLE +
                    " VALUES (" + sender.getId() + ", " + friend.getId() + ")");

            return SendFriendRequestResult.SENT_SUCCESSFULLY;
        } catch (SQLException e) {
            e.printStackTrace();
            return SendFriendRequestResult.SQL_EXCEPTION;
        }
    }

    public void acceptFriendRequest(User sender, User friend) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE sender_id = " + sender.getId() + " AND friend_id = " + friend.getId());
            if (!rs.next()) return;

            st.execute("DELETE FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE sender_id = " + sender.getId() + " AND friend_id = " + friend.getId());

            addFriend(sender, friend);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void denyFriendRequest(User sender, User friend) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE sender_id = " + sender.getId() + " AND friend_id = " + friend.getId());
            if (!rs.next()) return;

            st.execute("DELETE FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE sender_id = " + sender.getId() + " AND friend_id = " + friend.getId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Chat createChat(String name, List<User> members) {
        try (Statement st = con.createStatement()) {
            st.execute("INSERT INTO " + Constants.CHATS_TABLE + " (name)" +
                    " VALUES ('" + name + "')");
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.CHATS_TABLE +
                    " WHERE chat_id = LAST_INSERT_ID()");
            rs.next();
            long chatID = rs.getLong("chat_id");
            Timestamp dateCreated = rs.getTimestamp("date_created");
            Chat c = new Chat(chatID, name, dateCreated, 0);

            for (User u : members) {
                st.execute("INSERT INTO " + Constants.USERS_IN_CHATS_TABLE +
                        " VALUES (" + chatID + ", " + u.getId() + ")");
                c.getMembers().add(u);
            }

            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Chat(-1, "dupa", Timestamp.valueOf(LocalDateTime.MIN), -1);
        }
    }

    public User getUser(long id) {
        return getUser0(String.valueOf(id), "user_id");
    }

    public User getUser(String name) {
        return getUser0(name, "username");
    }

    private User getUser0(String arg, String argName) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.USERS_TABLE + " WHERE " + argName + " = '" + arg + "'");
            if (!rs.next()) return null;

            long id = rs.getLong("user_id");
            String nickname = rs.getString("username");
            String hashedPassword = rs.getString("password_hash");
            Timestamp joined = rs.getTimestamp("date_joined");

            User returned = new User(id, nickname, hashedPassword, joined);

            rs = st.executeQuery("SELECT friend_id FROM " + Constants.FRIENDS_TABLE + " WHERE user_id = " + id);

            while (rs.next()) returned.addFriend(rs.getLong("friend_id"));

            return returned;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUsers() {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.USERS_TABLE);
            List<User> allUsers = new ArrayList<>();
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
            ResultSet rs = st.executeQuery("SELECT chat_id FROM " + Constants.CHATS_TABLE);
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
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.CHATS_TABLE +
                    " WHERE chat_id = " + chatID);
            if (!rs.next()) return null;
            String name = rs.getString("name");
            Timestamp created = rs.getTimestamp("date_created");

            Chat c = new Chat(chatID, name, created, getChatMessageCounter(chatID));

            getMessagesFromChat(chatID, 0, Constants.DEFAULT_MESSAGES_LIMIT)
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
            ResultSet rs = st.executeQuery("SELECT * FROM " + Constants.MESSAGES_TABLE +
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
            ResultSet rs = st.executeQuery("SELECT message_counter FROM " + Constants.CHATS_MESSAGE_COUNTERS_TABLE +
                    " WHERE chat_id = " + chatID);
            if (!rs.next()) return -1;

            return rs.getLong("message_counter");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Message> getMessagesFromChat(long chatID, long offset, long limit) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(
                    "SELECT message_id " +
                        "FROM " + Constants.MESSAGES_TABLE + " " +
                        "WHERE chat_id = " + chatID + " " +
                        "LIMIT " + offset + ", " + limit
            );
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

    public List<User> getUsersInChat(long chatID) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT user_id FROM " + Constants.USERS_IN_CHATS_TABLE +
                    " WHERE chat_id = " + chatID);
            List<User> users = new ArrayList<>();

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
            ResultSet rs = st.executeQuery("SELECT chat_id FROM " + Constants.USERS_IN_CHATS_TABLE +
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

    public boolean isFriend(User user, User friend) {
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT friend_id FROM " + Constants.FRIENDS_TABLE +
                    " WHERE user_id = " + user.getId());
            while (rs.next()) {
                long friendID = rs.getLong("friend_id");
                if (friend.getId() == friendID) return true;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getFriends(User user) {
        try (Statement st = con.createStatement()) {
            List<User> friends = new ArrayList<>();

            ResultSet rs = st.executeQuery("SELECT friend_id FROM " + Constants.FRIENDS_TABLE +
                    " WHERE user_id = " + user.getId());

            while (rs.next()) {
                long id = rs.getLong("friend_id");
                friends.add(getUser(id));
            }

            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<User> getFriendRequests(User user) {
        try (Statement st = con.createStatement()) {
            List<User> friendRequests = new ArrayList<>();

            ResultSet rs = st.executeQuery("SELECT sender_id FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE friend_id = " + user.getId());

            while (rs.next()) {
                long id = rs.getLong("sender_id");
                friendRequests.add(getUser(id));
            }

            return friendRequests;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

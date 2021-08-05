package com.github.jacekpoz.server;

import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.EnumResults.*;
import com.kosprov.jargon2.api.Jargon2;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public DatabaseConnector(String url, String dbUsername, String dbPassword) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(url, dbUsername, dbPassword);
    }

    public RegisterResult register(String username, String hash) {
        try (PreparedStatement checkUsername = con.prepareStatement(
                "SELECT username " +
                    "FROM " + Constants.USERS_TABLE +
                    " WHERE username = ?;"
        )) {
            checkUsername.setString(1, username);
            ResultSet rs = checkUsername.executeQuery();
            if (rs.next()) {
                rs.close();
                return RegisterResult.USERNAME_TAKEN;
            }
            rs.close();

            createUser(username, hash);

            return RegisterResult.ACCOUNT_CREATED;
        } catch (SQLException e) {
            e.printStackTrace();
            return RegisterResult.SQL_EXCEPTION;
        }
    }

    public LoginResult login(String username, byte[] password) {
        try (PreparedStatement checkUsername = con.prepareStatement(
                "SELECT * " +
                    "FROM " + Constants.USERS_TABLE +
                    " WHERE username = ?;"
        )) {
            checkUsername.setString(1, username);
            ResultSet rs = checkUsername.executeQuery();
            if (!rs.next()) {
                rs.close();
                return LoginResult.ACCOUNT_DOESNT_EXIST;
            }

            String dbHash = rs.getString("password_hash");
            rs.close();

            Jargon2.Verifier v = Jargon2.jargon2Verifier();
            if (v.hash(dbHash).password(password).verifyEncoded()) {
                return LoginResult.LOGGED_IN;
            }

            return LoginResult.WRONG_PASSWORD;
        } catch (SQLException e) {
            e.printStackTrace();
            return LoginResult.SQL_EXCEPTION;
        }
    }

    public AddFriendResult addFriend(long userID, long friendID) {
        if (userID == friendID) return AddFriendResult.SAME_USER;

        PreparedStatement insertFriend = null;
        try (PreparedStatement checkFriend = con.prepareStatement(
                "SELECT * " +
                    "FROM " + Constants.FRIENDS_TABLE +
                    " WHERE user_id = ? AND friend_id = ?;"
        )) {
            checkFriend.setLong(1, userID);
            checkFriend.setLong(2, friendID);
            ResultSet rs = checkFriend.executeQuery();
            if (rs.next()) {
                rs.close();
                return AddFriendResult.ALREADY_FRIEND;
            }
            rs.close();

            insertFriend = con.prepareStatement(
                    "INSERT INTO " + Constants.FRIENDS_TABLE +
                        " VALUES (?, ?);"
            );
            insertFriend.setLong(1, userID);
            insertFriend.setLong(2, friendID);
            insertFriend.execute();

            return AddFriendResult.ADDED_FRIEND;
        } catch (SQLException e) {
            e.printStackTrace();
            return AddFriendResult.SQL_EXCEPTION;
        } finally {
            try {
                if (insertFriend != null) insertFriend.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public RemoveFriendResult removeFriend(long userID, long friendID) {
        if (userID == friendID) return RemoveFriendResult.SAME_USER;

        try (PreparedStatement removeFriend = con.prepareStatement(
                "DELETE FROM " + Constants.FRIENDS_TABLE +
                " WHERE user_id = ? AND friend_id = ?;"
        )) {
            removeFriend.setLong(1, userID);
            removeFriend.setLong(2, friendID);
            removeFriend.execute();

            return RemoveFriendResult.REMOVED_FRIEND;
        } catch (SQLException e) {
            e.printStackTrace();
            return RemoveFriendResult.SQL_EXCEPTION;
        }
    }

    public void addMessage(long messageID, long chatID, long authorID, String content) {
        try (PreparedStatement addMessage = con.prepareStatement(
                "INSERT INTO " + Constants.MESSAGES_TABLE + "(message_id, chat_id, author_id, content)" +
                    "VALUES (?, ?, ?, ?);"
        )) {
            addMessage.setLong(1, messageID);
            addMessage.setLong(2, chatID);
            addMessage.setLong(3, authorID);
            addMessage.setString(4, content);
            addMessage.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SendFriendRequestResult sendFriendRequest(long senderID, long friendID) {
        if (senderID == friendID) return SendFriendRequestResult.SAME_USER;
        if (isFriend(senderID, friendID)) return SendFriendRequestResult.ALREADY_FRIENDS;

        PreparedStatement insertRequest = null;
        try (PreparedStatement checkRequest = con.prepareStatement(
                "SELECT * " +
                    "FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE sender_id = ? AND friend_id = ?;"
        )) {
            checkRequest.setLong(1, senderID);
            checkRequest.setLong(2, friendID);
            ResultSet rs = checkRequest.executeQuery();
            if (rs.next()) {
                rs.close();
                return SendFriendRequestResult.ALREADY_SENT;
            }
            rs.close();

            insertRequest = con.prepareStatement(
                    "INSERT INTO " + Constants.FRIEND_REQUESTS_TABLE +
                        " VALUES (?, ?);"
            );
            insertRequest.setLong(1, senderID);
            insertRequest.setLong(2, friendID);
            insertRequest.execute();

            return SendFriendRequestResult.SENT_SUCCESSFULLY;
        } catch (SQLException e) {
            e.printStackTrace();
            return SendFriendRequestResult.SQL_EXCEPTION;
        } finally {
            try {
                if (insertRequest != null) insertRequest.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void acceptFriendRequest(long senderID, long friendID) {
        if (senderID == friendID) return;

        PreparedStatement deleteRequest = null;
        try (PreparedStatement checkRequest = con.prepareStatement(
                "SELECT * " +
                    "FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE sender_id = ? AND friend_id = ?;"
        )) {
            checkRequest.setLong(1, senderID);
            checkRequest.setLong(2, friendID);

            ResultSet rs = checkRequest.executeQuery();
            if (!rs.next()) {
                rs.close();
                return;
            }
            rs.close();

            deleteRequest = con.prepareStatement(
                    "DELETE FROM " + Constants.FRIEND_REQUESTS_TABLE +
                        " WHERE sender_id = ? AND friend_id = ?;"
            );
            deleteRequest.setLong(1, senderID);
            deleteRequest.setLong(2, friendID);
            deleteRequest.execute();

            addFriend(senderID, friendID);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (deleteRequest != null) deleteRequest.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void denyFriendRequest(long senderID, long friendID) {
        if (senderID == friendID) return;

        PreparedStatement deleteRequest = null;
        try (PreparedStatement checkRequest = con.prepareStatement(
                "SELECT * " +
                    "FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE sender_id = ? AND friend_id = ?;"
        )) {
            checkRequest.setLong(1, senderID);
            checkRequest.setLong(2, friendID);
            ResultSet rs = checkRequest.executeQuery();
            if (!rs.next()) {
                rs.close();
                return;
            }
            rs.close();

            deleteRequest = con.prepareStatement(
                    "DELETE FROM " + Constants.FRIEND_REQUESTS_TABLE +
                        " WHERE sender_id = ? AND friend_id = ?;"
            );
            deleteRequest.setLong(1, senderID);
            deleteRequest.setLong(2, friendID);
            deleteRequest.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (deleteRequest != null) deleteRequest.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Chat createChat(String name, List<Long> memberIDs) {
        PreparedStatement selectMissingInfo = null;

        try (PreparedStatement insertChat = con.prepareStatement(
                "INSERT INTO " + Constants.CHATS_TABLE + " (name)" +
                    " VALUES (?)"
        )) {
            insertChat.setString(1, name);
            insertChat.execute();

            selectMissingInfo = con.prepareStatement(
                    "SELECT * " +
                        "FROM " + Constants.CHATS_TABLE +
                        " WHERE chat_id = LAST_INSERT_ID();"
            );
            ResultSet rs = selectMissingInfo.executeQuery();
            rs.next();
            long chatID = rs.getLong("chat_id");
            Timestamp dateCreated = rs.getTimestamp("date_created");
            rs.close();
            Chat c = new Chat(chatID, name, dateCreated.toLocalDateTime(), 0);

            for (long id : memberIDs) {
                PreparedStatement insertMember = con.prepareStatement(
                        "INSERT INTO " + Constants.USERS_IN_CHATS_TABLE +
                            " VALUES (?, ?);"
                );
                insertMember.setLong(1, chatID);
                insertMember.setLong(2, id);
                insertMember.execute();
                insertMember.close();
                c.getMemberIDs().add(id);
            }

            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Chat(-1, "dupa", LocalDateTime.MIN, -1);
        } finally {
            try {
                if (selectMissingInfo != null) selectMissingInfo.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public User createUser(String username, String hash) {
        try (PreparedStatement insertUser = con.prepareStatement(
                "INSERT INTO " + Constants.USERS_TABLE + "(username, password_hash)" +
                " VALUES (?, ?);"
        )) {
            insertUser.setString(1, username);
            insertUser.setString(2, hash);
            insertUser.execute();

            return getUser(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUser(long id) {
        return getUser0(String.valueOf(id), "user_id");
    }

    public User getUser(String name) {
        return getUser0(name, "username");
    }

    private User getUser0(String arg, String columnName) {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT * " +
                    "FROM " + Constants.USERS_TABLE +
                    " WHERE " + columnName + " = ?;"
        )) {
            st.setString(1, arg);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) return null;

            long id = rs.getLong("user_id");
            String nickname = rs.getString("username");
            String hashedPassword = rs.getString("password_hash");
            Timestamp joined = rs.getTimestamp("date_joined");
            rs.close();

            User returned = new User(id, nickname, hashedPassword, joined.toLocalDateTime());

            List<User> friends = getFriends(id);
            friends.forEach(returned::addFriend);

            return returned;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> getAllUsers() {
        try (PreparedStatement getUsers = con.prepareStatement(
                "SELECT * " +
                    "FROM " + Constants.USERS_TABLE
        )) {
            ResultSet rs = getUsers.executeQuery();
            List<User> allUsers = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("user_id");
                String nickname = rs.getString("username");
                String hashedPassword = rs.getString("password_hash");
                Timestamp joined = rs.getTimestamp("date_joined");

                allUsers.add(new User(id, nickname, hashedPassword, joined.toLocalDateTime()));
            }
            rs.close();

            return allUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Chat> getAllChats() {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT chat_id " +
                    "FROM " + Constants.CHATS_TABLE
        )) {
            ResultSet rs = st.executeQuery();
            List<Chat> allChats = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("chat_id");
                allChats.add(getChat(id));
            }
            rs.close();

            return allChats;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Chat getChat(long chatID) {
        try (PreparedStatement getChat = con.prepareStatement(
                "SELECT * " +
                    "FROM " + Constants.CHATS_TABLE +
                    " WHERE chat_id = ?;"
        )) {
            getChat.setLong(1, chatID);
            ResultSet rs = getChat.executeQuery();
            if (!rs.next()) return null;
            String name = rs.getString("name");
            Timestamp created = rs.getTimestamp("date_created");
            rs.close();

            Chat c = new Chat(chatID, name, created.toLocalDateTime(), getChatMessageCounter(chatID));

            getMessagesFromChat(chatID, 0, Constants.DEFAULT_MESSAGES_LIMIT)
                    .forEach(message -> c.getMessages().add(message));

            getUsersInChat(chatID)
                    .forEach(user -> c.getMemberIDs().add(user.getId()));

            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message getMessage(long messageID, long chatID) {
        try (PreparedStatement getMessage = con.prepareStatement(
                "SELECT * " +
                    "FROM " + Constants.MESSAGES_TABLE +
                    " WHERE message_id = ? AND chat_id = ?;"
        )) {
            getMessage.setLong(1, messageID);
            getMessage.setLong(2, chatID);
            ResultSet rs = getMessage.executeQuery();
            if (!rs.next()) return new Message(String.format("Message{messageID=%s,chatID=%s} doesn't exist",
                    messageID, chatID));

            long authorID = rs.getLong("author_id");
            String content = rs.getString("content");
            Timestamp sendDate = rs.getTimestamp("date_sent");
            rs.close();

            return new Message(messageID, chatID, authorID, content, sendDate.toLocalDateTime());
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message("getMessage() failed");
        }
    }

    public long getChatMessageCounter(long chatID) {
        try (PreparedStatement getMessageCounter = con.prepareStatement(
                "SELECT message_counter " +
                    "FROM " + Constants.CHATS_MESSAGE_COUNTERS_TABLE +
                    " WHERE chat_id = ?;"
        )) {
            getMessageCounter.setLong(1, chatID);
            ResultSet rs = getMessageCounter.executeQuery();
            long counter;
            if (!rs.next()) counter = -1;
            else counter = rs.getLong("message_counter");
            rs.close();

            return counter;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Message> getMessagesFromChat(long chatID, long offset, long limit) {
        try (PreparedStatement getMessageIDs = con.prepareStatement(
                "SELECT message_id " +
                    "FROM " + Constants.MESSAGES_TABLE +
                    " WHERE chat_id = ? " +
                    "LIMIT ?, ?;"
        )) {
            getMessageIDs.setLong(1, chatID);
            getMessageIDs.setLong(2, offset);
            getMessageIDs.setLong(3, limit);

            ResultSet rs = getMessageIDs.executeQuery();
            List<Message> messages = new ArrayList<>();

            while (rs.next()) {
                long messageID = rs.getLong("message_id");
                messages.add(getMessage(messageID, chatID));
            }
            rs.close();

            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Message> getMessagesFromChat(long chatID, long offset, long limit, long authorID) {
        return getMessagesFromChat(chatID, offset, limit)
                .stream()
                .filter(m -> m.getAuthorID() == authorID)
                .collect(Collectors.toList());
    }

    public List<User> getUsersInChat(long chatID) {
        try (PreparedStatement getUserIDs = con.prepareStatement(
                "SELECT user_id " +
                    "FROM " + Constants.USERS_IN_CHATS_TABLE +
                    " WHERE chat_id = ?;"
        )) {
            getUserIDs.setLong(1, chatID);
            ResultSet rs = getUserIDs.executeQuery();
            List<User> users = new ArrayList<>();

            while (rs.next()) {
                long userID = rs.getLong("user_id");
                users.add(getUser(userID));
            }
            rs.close();

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Chat> getUsersChats(long userID) {
        try (PreparedStatement getChatIDs = con.prepareStatement(
                "SELECT chat_id " +
                    "FROM " + Constants.USERS_IN_CHATS_TABLE +
                    " WHERE user_id = ?;"
        )) {
            getChatIDs.setLong(1, userID);
            ResultSet rs = getChatIDs.executeQuery();
            List<Chat> chats = new ArrayList<>();

            while (rs.next()) {
                long chatID = rs.getLong("chat_id");
                chats.add(getChat(chatID));
            }
            rs.close();

            return chats;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean isFriend(long userID, long friendID) {
        try (PreparedStatement getFriendIDs = con.prepareStatement(
                "SELECT friend_id " +
                    "FROM " + Constants.FRIENDS_TABLE +
                    " WHERE user_id = ?;"
        )) {
            getFriendIDs.setLong(1, userID);
            ResultSet rs = getFriendIDs.executeQuery();
            while (rs.next()) {
                long dbFriendID = rs.getLong("friend_id");
                if (friendID == dbFriendID) return true;
            }
            rs.close();

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getFriends(long userID) {
        try (PreparedStatement getFriendIDs = con.prepareStatement(
                "SELECT friend_id " +
                    "FROM " + Constants.FRIENDS_TABLE +
                    " WHERE user_id = ?;"
        )) {
            getFriendIDs.setLong(1, userID);
            List<User> friends = new ArrayList<>();

            ResultSet rs = getFriendIDs.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("friend_id");
                friends.add(getUser(id));
            }
            rs.close();

            return friends;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<User> getFriendRequests(long userID) {
        try (PreparedStatement st = con.prepareStatement(
                "SELECT sender_id " +
                    "FROM " + Constants.FRIEND_REQUESTS_TABLE +
                    " WHERE friend_id = ?;"
        )) {
            st.setLong(1, userID);
            List<User> friendRequests = new ArrayList<>();

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                long id = rs.getLong("sender_id");
                friendRequests.add(getUser(id));
            }
            rs.close();

            return friendRequests;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

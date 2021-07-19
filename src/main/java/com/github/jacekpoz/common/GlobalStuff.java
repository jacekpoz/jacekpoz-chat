package com.github.jacekpoz.common;

public class GlobalStuff {
    public static long CHANNEL_COUNTER = 0L;
    public static long MESSAGE_COUNTER = 0L;

    public static String SERVER_HOST = "localhost";
    public static int SERVER_PORT = 2137;
    public static int DB_PORT = 3306;

    public static final String DB_NAME = "mydatabase";
    public static final String USERS_TABLE = "users";
    public static final String FRIENDS_TABLE = "friends";
    public static final String CHATS_TABLE = "chats";
    public static final String MESSAGES_TABLE = "messages";
    public static final String CHATS_MESSAGE_COUNTERS_TABLE = "chats_message_counters";
    public static final String USERS_IN_CHATS_TABLE = "users_in_chats";
}

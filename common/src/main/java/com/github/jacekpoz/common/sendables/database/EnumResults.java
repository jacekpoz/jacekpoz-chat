package com.github.jacekpoz.common.sendables.database;

public class EnumResults {

    public enum LoginResult {
        LOGGED_IN,
        ACCOUNT_DOESNT_EXIST,
        WRONG_PASSWORD,
        SQL_EXCEPTION
    }

    public enum RegisterResult {
        USERNAME_TAKEN,
        ACCOUNT_CREATED,
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

    public enum SendFriendRequestResult {
        SENT_SUCCESSFULLY,
        ALREADY_SENT,
        ALREADY_FRIENDS,
        SAME_USER,
        SQL_EXCEPTION
    }
}

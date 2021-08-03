package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

public class GetUserQuery implements UserQuery {

    private final long userID;
    @Getter
    private final String username;
    private final long callerID;

    protected GetUserQuery(long userID, String username, long callerID) {
        this.userID = userID;
        this.username = username;
        this.callerID = callerID;
    }

    public GetUserQuery(long userID, long callerID) {
        this(userID, null, callerID);
    }

    public GetUserQuery(String username, long callerID) {
        this(-1, username, callerID);
    }

    @Override
    public long getUserID() {
        return userID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }
}

package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class DeleteUserQuery implements UserQuery {

    private final long userID;
    private final long callerID;

    public DeleteUserQuery(long userID, long callerID) {
        this.userID = userID;
        this.callerID = callerID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }

    @Override
    public long getUserID() {
        return userID;
    }
}

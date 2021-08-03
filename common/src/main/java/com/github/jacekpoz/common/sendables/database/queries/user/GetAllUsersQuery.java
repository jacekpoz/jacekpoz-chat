package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class GetAllUsersQuery implements UserQuery {

    private final long callerID;

    public GetAllUsersQuery(long callerID) {
        this.callerID = callerID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }

    @Override
    public long getUserID() {
        return -1;
    }
}

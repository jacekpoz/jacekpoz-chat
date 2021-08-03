package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

public class RegisterQuery implements UserQuery {

    @Getter
    private final String username;
    @Getter
    private final String hash;
    private final long callerID;

    public RegisterQuery(String username, String hash, long callerID) {
        this.username = username;
        this.hash = hash;
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

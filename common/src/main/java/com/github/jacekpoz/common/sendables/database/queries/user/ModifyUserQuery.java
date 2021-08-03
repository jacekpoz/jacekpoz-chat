package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

public class ModifyUserQuery implements UserQuery {

    private final long userID;
    @Getter
    private final String columnToModify;
    @Getter
    private final String newValue;
    private final long callerID;

    public ModifyUserQuery(long userID, String columnToModify, String newValue, long callerID) {
        this.userID = userID;
        this.columnToModify = columnToModify;
        this.newValue = newValue;
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

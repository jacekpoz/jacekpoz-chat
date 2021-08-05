package com.github.jacekpoz.common.sendables.database.queries.interfaces;

import com.github.jacekpoz.common.sendables.User;
import lombok.Getter;

public class UserQuery implements Query<User> {

    @Getter
    private final long userID;
    private final long callerID;

    public UserQuery(long userID, long callerID) {
        this.userID = userID;
        this.callerID = callerID;
    }

    public UserQuery() {
        this(-1, -1);
    }

    @Override
    public long getCallerID() {
        return callerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserQuery userQuery = (UserQuery) o;
        return userID == userQuery.userID && callerID == userQuery.callerID;
    }

    @Override
    public String toString() {
        return "UserQuery{" +
                "userID=" + userID +
                ", callerID=" + callerID +
                '}';
    }
}

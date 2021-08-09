package com.github.jacekpoz.common.sendables.database.queries.basequeries;

import com.github.jacekpoz.common.sendables.User;
import lombok.Getter;

public abstract class UserQuery implements Query<User> {

    @Getter
    private final long userID;
    private final long callerID;

    public UserQuery(long userID, long callerID) {
        this.userID = userID;
        this.callerID = callerID;
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

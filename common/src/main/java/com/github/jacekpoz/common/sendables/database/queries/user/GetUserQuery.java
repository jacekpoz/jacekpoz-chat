package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

import java.util.Objects;

public class GetUserQuery extends UserQuery {

    @Getter
    private final String username;

    protected GetUserQuery(long userID, String username, long callerID) {
        super(userID, callerID);
        this.username = username;
    }

    public GetUserQuery(long userID, long callerID) {
        this(userID, null, callerID);
    }

    public GetUserQuery(String username, long callerID) {
        this(-1, username, callerID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetUserQuery)) return false;
        if (!super.equals(o)) return false;
        GetUserQuery that = (GetUserQuery) o;
        return getUserID() == that.getUserID() &&
                Objects.equals(username, that.username) &&
                getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "GetUserQuery{" +
                "userID=" + getUserID() +
                ", username='" + username + '\'' +
                ", callerID=" + getCallerID() +
                '}';
    }
}

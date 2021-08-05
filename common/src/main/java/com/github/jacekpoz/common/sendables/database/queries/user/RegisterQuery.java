package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

import java.util.Objects;

public class RegisterQuery extends UserQuery {

    @Getter
    private final String username;
    @Getter
    private final String hash;

    public RegisterQuery(String username, String hash, long callerID) {
        super(-1, callerID);
        this.username = username;
        this.hash = hash;
    }

    @Override
    public long getUserID() {
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterQuery)) return false;
        if (!super.equals(o)) return false;
        RegisterQuery that = (RegisterQuery) o;
        return getUserID() == that.getUserID() &&
                Objects.equals(username, that.username) &&
                Objects.equals(hash, that.hash) &&
                getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "RegisterQuery{" +
                "username='" + username + '\'' +
                ", hash='" + hash + '\'' +
                ", callerID=" + getCallerID() +
                '}';
    }
}

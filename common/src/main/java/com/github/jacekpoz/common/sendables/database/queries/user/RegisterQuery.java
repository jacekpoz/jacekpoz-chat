package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

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
}

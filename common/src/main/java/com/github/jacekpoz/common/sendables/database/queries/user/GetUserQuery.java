package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

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

}

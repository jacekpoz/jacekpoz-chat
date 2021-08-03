package com.github.jacekpoz.common.sendables.database.queries.user;

import lombok.Getter;

public class LoginQuery extends GetUserQuery {

    @Getter
    private final char[] password;

    public LoginQuery(String username, char[] password, long callerID) {
        super(username, callerID);
        this.password = password;
    }
}

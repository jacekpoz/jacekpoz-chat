package com.github.jacekpoz.common.database.queries;

import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.User;
import lombok.Getter;

public class UserQuery implements Query<User> {

    // query types
    // ****************************************
    public static final int SINGLE_USER    = 0;
    public static final int USERS_FRIENDS  = 1;
    public static final int USERS_IN_CHAT  = 2;
    // ****************************************

    @Getter
    private final long id;
    @Getter
    private final int type;

    public UserQuery(long id, int type) {
        this.id = id;
        this.type = type;
    }
}

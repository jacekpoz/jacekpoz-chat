package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.InsertQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

public class InsertUserQuery extends InsertQuery<User> implements UserQuery {

    @Getter
    private final String username;
    @Getter
    private final String hash;

    public InsertUserQuery(String username, String hash, Screen caller) {
        super(caller);
        this.username = username;
        this.hash = hash;
    }
}

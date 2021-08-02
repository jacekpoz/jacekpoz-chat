package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.DeleteQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class DeleteUserQuery extends DeleteQuery<User> implements UserQuery {

    public DeleteUserQuery(long userID, Screen caller) {
        super(userID, caller);
    }
}

package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.ModifyQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class ModifyUserQuery extends ModifyQuery<User> implements UserQuery {

    public ModifyUserQuery(long userID, String columnToModify, String newValue, Screen caller) {
        super(userID, columnToModify, newValue, caller);
    }
}

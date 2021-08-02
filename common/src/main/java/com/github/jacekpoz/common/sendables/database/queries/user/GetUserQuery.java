package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.GetQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class GetUserQuery extends GetQuery<User> implements UserQuery {

    public GetUserQuery(long userID, Screen caller) {
        super(userID, caller);
    }
}

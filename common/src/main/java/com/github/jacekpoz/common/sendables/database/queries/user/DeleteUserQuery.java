package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class DeleteUserQuery extends UserQuery {

    public DeleteUserQuery(long userID, long callerID) {
        super(userID, callerID);
    }

}

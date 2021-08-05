package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class GetAllUsersQuery extends UserQuery {

    public GetAllUsersQuery(long callerID) {
        super(-1, callerID);
    }

    @Override
    public long getUserID() {
        return -1;
    }
}

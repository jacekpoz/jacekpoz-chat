package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class GetFriendsQuery extends UserQuery {

    public GetFriendsQuery(long userID, long callerID) {
        super(userID, callerID);
    }

}

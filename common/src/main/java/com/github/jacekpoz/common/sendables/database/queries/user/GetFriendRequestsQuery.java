package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class GetFriendRequestsQuery extends UserQuery {

    public GetFriendRequestsQuery(long userID, long callerID) {
        super(userID, callerID);
    }

    @Override
    public String toString() {
        return "GetFriendRequestsQuery{" +
                "userID=" + getUserID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

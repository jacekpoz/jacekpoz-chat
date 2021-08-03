package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

public class GetFriendRequestsQuery implements UserQuery {

    @Getter
    private final User user;
    private final long callerID;

    public GetFriendRequestsQuery(User user, long callerID) {
        this.user = user;
        this.callerID = callerID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }

    @Override
    public long getUserID() {
        return user.getId();
    }
}

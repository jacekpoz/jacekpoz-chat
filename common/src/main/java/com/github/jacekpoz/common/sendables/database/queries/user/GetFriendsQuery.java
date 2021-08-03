package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

public class GetFriendsQuery implements UserQuery {

    @Getter
    private final User user;
    private final long callerID;

    public GetFriendsQuery(User user, long callerID) {
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

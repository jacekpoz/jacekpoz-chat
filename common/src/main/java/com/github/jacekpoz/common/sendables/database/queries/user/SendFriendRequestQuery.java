package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

public class SendFriendRequestQuery implements UserQuery {

    @Getter
    private final User sender;
    @Getter
    private final User friend;
    private final long callerID;

    public SendFriendRequestQuery(User sender, User friend, long callerID) {
        this.sender = sender;
        this.friend = friend;
        this.callerID = callerID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }

    @Override
    public long getUserID() {
        return sender.getId();
    }
}

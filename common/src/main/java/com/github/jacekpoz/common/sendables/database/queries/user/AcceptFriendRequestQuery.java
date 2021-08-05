package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

public class AcceptFriendRequestQuery extends UserQuery {

    @Getter
    private final long friendID;

    public AcceptFriendRequestQuery(long senderID, long friendID, long callerID) {
        super(senderID, callerID);
        this.friendID = friendID;
    }

}

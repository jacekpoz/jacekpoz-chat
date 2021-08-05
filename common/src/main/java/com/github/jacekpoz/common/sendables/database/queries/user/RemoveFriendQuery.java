package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

public class RemoveFriendQuery extends UserQuery {

    @Getter
    private final long friendID;

    public RemoveFriendQuery(long userID, long friendID, long callerID) {
        super(userID, callerID);
        this.friendID = friendID;
    }

}

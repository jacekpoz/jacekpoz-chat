package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

import java.util.Objects;

public class DenyFriendRequestQuery extends UserQuery {

    @Getter
    private final long friendID;

    public DenyFriendRequestQuery(long senderID, long friendID, long callerID) {
        super(senderID, callerID);
        this.friendID = friendID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DenyFriendRequestQuery)) return false;
        if (!super.equals(o)) return false;
        DenyFriendRequestQuery that = (DenyFriendRequestQuery) o;
        return getUserID() == that.getUserID() &&
                friendID == that.friendID &&
                getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "DenyFriendRequestQuery{" +
                "senderID=" + getUserID() +
                ", friendID=" + friendID +
                ", callerID=" + getCallerID() +
                '}';
    }
}

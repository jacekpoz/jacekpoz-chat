package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

import java.util.Objects;

public class SendFriendRequestQuery extends UserQuery {

    @Getter
    private final long friendID;

    public SendFriendRequestQuery(long senderID, long friendID, long callerID) {
        super(senderID, callerID);
        this.friendID = friendID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SendFriendRequestQuery that = (SendFriendRequestQuery) o;
        return getUserID() == that.getUserID() && friendID == that.friendID && getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "SendFriendRequestQuery{" +
                "senderID=" + getUserID() +
                ", friendID=" + friendID +
                ", callerID=" + getCallerID() +
                '}';
    }
}

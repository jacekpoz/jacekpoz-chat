package com.github.jacekpoz.common.sendables.database.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;
import lombok.Getter;

public class DenyFriendRequestQuery extends UserQuery {

    @Getter
    private final long friendID;

    @JsonCreator
    public DenyFriendRequestQuery(
            @JsonProperty("senderID") long senderID,
            @JsonProperty("friendID") long friendID,
            @JsonProperty("callerID") long callerID
    ) {
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

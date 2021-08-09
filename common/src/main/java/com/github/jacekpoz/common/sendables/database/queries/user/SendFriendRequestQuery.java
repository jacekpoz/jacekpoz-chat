package com.github.jacekpoz.common.sendables.database.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;
import lombok.Getter;

public class SendFriendRequestQuery extends UserQuery {

    @Getter
    private final long friendID;

    @JsonCreator
    public SendFriendRequestQuery(
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

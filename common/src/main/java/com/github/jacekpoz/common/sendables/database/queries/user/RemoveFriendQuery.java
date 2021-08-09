package com.github.jacekpoz.common.sendables.database.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;
import lombok.Getter;

public class RemoveFriendQuery extends UserQuery {

    @Getter
    private final long friendID;

    @JsonCreator
    public RemoveFriendQuery(
            @JsonProperty("userID") long userID,
            @JsonProperty("friendID") long friendID,
            @JsonProperty("callerID") long callerID
    ) {
        super(userID, callerID);
        this.friendID = friendID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemoveFriendQuery)) return false;
        if (!super.equals(o)) return false;
        RemoveFriendQuery that = (RemoveFriendQuery) o;
        return getUserID() == that.getUserID() && friendID == that.friendID && getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "RemoveFriendQuery{" +
                "userID=" + getUserID() +
                ", friendID=" + friendID +
                ", callerID=" + getCallerID() +
                '}';
    }
}

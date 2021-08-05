package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

import java.util.Objects;

public class RemoveFriendQuery extends UserQuery {

    @Getter
    private final long friendID;

    public RemoveFriendQuery(long userID, long friendID, long callerID) {
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

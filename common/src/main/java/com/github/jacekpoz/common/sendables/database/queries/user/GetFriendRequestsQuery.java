package com.github.jacekpoz.common.sendables.database.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;

public class GetFriendRequestsQuery extends UserQuery {

    @JsonCreator
    public GetFriendRequestsQuery(
            @JsonProperty("userID") long userID,
            @JsonProperty("callerID") long callerID
    ) {
        super(userID, callerID);
    }

    @Override
    public String toString() {
        return "GetFriendRequestsQuery{" +
                "userID=" + getUserID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

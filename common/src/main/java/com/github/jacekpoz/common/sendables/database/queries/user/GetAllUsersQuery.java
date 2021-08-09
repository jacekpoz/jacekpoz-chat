package com.github.jacekpoz.common.sendables.database.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;

public class GetAllUsersQuery extends UserQuery {

    @JsonCreator
    public GetAllUsersQuery(
            @JsonProperty("callerID") long callerID
    ) {
        super(-1, callerID);
    }

    @Override
    public long getUserID() {
        return -1;
    }

    @Override
    public String toString() {
        return "GetAllUsersQuery{" +
                "userID=" + getUserID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

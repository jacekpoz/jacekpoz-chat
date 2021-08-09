package com.github.jacekpoz.common.sendables.database.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;

public class DeleteUserQuery extends UserQuery {

    @JsonCreator
    public DeleteUserQuery(
            @JsonProperty("userID") long userID,
            @JsonProperty("callerID") long callerID
    ) {
        super(userID, callerID);
    }

    @Override
    public String toString() {
        return "DeleteUserQuery{" +
                "userID=" + getUserID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

package com.github.jacekpoz.common.sendables.database.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;

public class GetUsersInChatQuery extends UserQuery {

    @JsonCreator
    public GetUsersInChatQuery(
            @JsonProperty("chatID") long chatID,
            @JsonProperty("callerID") long callerID
    ) {
        super(chatID, callerID);
    }

    public long getChatID() {
        return getUserID();
    }

    @Override
    public String toString() {
        return "GetUsersInChatQuery{" +
                "chatID=" + getChatID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

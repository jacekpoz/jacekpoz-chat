package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUsersChatsQuery extends GetChatQuery {

    @JsonCreator
    public GetUsersChatsQuery(
            @JsonProperty("userID") long userID,
            @JsonProperty("callerID") long callerID
    ) {
        super(userID, callerID);
    }

    public long getUserID() {
        return getChatID();
    }

    @Override
    public String toString() {
        return "GetUsersChatsQuery{" +
                "userID=" + getUserID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

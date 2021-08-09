package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.ChatQuery;

public class GetChatQuery extends ChatQuery {

    @JsonCreator
    public GetChatQuery(
            @JsonProperty("chatID") long chatID,
            @JsonProperty("callerID") long callerID
    ) {
        super(chatID, callerID);
    }

    @Override
    public String toString() {
        return "GetChatQuery{" +
                "chatID=" + getChatID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

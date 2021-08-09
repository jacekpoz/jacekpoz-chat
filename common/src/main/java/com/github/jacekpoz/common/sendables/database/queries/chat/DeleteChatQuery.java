package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.ChatQuery;

public class DeleteChatQuery extends ChatQuery {

    @JsonCreator
    public DeleteChatQuery(
            @JsonProperty("chatID") long chatID,
            @JsonProperty("callerID") long callerID
    ) {
        super(chatID, callerID);
    }

    @Override
    public String toString() {
        return "DeleteChatQuery{" +
                "chatID=" + getChatID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

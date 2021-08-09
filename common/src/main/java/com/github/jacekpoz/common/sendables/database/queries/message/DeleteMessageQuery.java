package com.github.jacekpoz.common.sendables.database.queries.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.MessageQuery;

public class DeleteMessageQuery extends MessageQuery {

    @JsonCreator
    public DeleteMessageQuery(
            @JsonProperty("messageID") long messageID,
            @JsonProperty("chatID") long chatID,
            @JsonProperty("callerID") long callerID
    ) {
        super(messageID, chatID, callerID);
    }

    public DeleteMessageQuery(long chatID, long callerID) {
        this(-1, chatID, callerID);
    }

    @Override
    public String toString() {
        return "DeleteMessageQuery{" +
                "messageID=" + getMessageID() +
                ", chatID=" + getChatID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

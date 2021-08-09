package com.github.jacekpoz.common.sendables.database.queries.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.MessageQuery;

public class GetMessageQuery extends MessageQuery {

    @JsonCreator
    public GetMessageQuery(
            @JsonProperty("messageID") long messageID,
            @JsonProperty("chatID") long chatID,
            @JsonProperty("callerID") long callerID
    ) {
        super(messageID, chatID, callerID);
    }

    @Override
    public String toString() {
        return "GetMessageQuery{" +
                "messageID=" + getMessageID() +
                ", chatID=" + getChatID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

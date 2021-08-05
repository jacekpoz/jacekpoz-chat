package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;

public class GetMessageQuery extends MessageQuery {

    public GetMessageQuery(long messageID, long chatID, long callerID) {
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

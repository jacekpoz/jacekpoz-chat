package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;

public class GetMessageQuery implements MessageQuery {

    private final long messageID;
    private final long chatID;
    private final long callerID;

    public GetMessageQuery(long messageID, long chatID, long callerID) {
        this.messageID = messageID;
        this.chatID = chatID;
        this.callerID = callerID;
    }

    @Override
    public long getMessageID() {
        return messageID;
    }

    @Override
    public long getChatID() {
        return chatID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }
}

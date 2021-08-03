package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;

public class DeleteMessageQuery implements MessageQuery {

    private final long messageID;
    private final long chatID;
    private final long callerID;

    public DeleteMessageQuery(long messageID, long chatID, long callerID) {
        this.messageID = messageID;
        this.chatID = chatID;
        this.callerID = callerID;
    }

    public DeleteMessageQuery(long chatID, long callerID) {
        this(-1, chatID, callerID);
    }

    @Override
    public long getChatID() {
        return chatID;
    }

    @Override
    public long getMessageID() {
        return messageID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }
}

package com.github.jacekpoz.common.sendables.database.queries.interfaces;

import com.github.jacekpoz.common.sendables.Message;
import lombok.Getter;

public abstract class MessageQuery implements Query<Message> {

    @Getter
    private final long messageID;
    @Getter
    private final long chatID;
    private final long callerID;

    public MessageQuery(long messageID, long chatID, long callerID) {
        this.messageID = messageID;
        this.chatID = chatID;
        this.callerID = callerID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }
}

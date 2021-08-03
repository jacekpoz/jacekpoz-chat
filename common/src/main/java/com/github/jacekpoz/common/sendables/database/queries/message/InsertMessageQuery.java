package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

public class InsertMessageQuery implements MessageQuery {

    @Getter
    private final Message message;
    private final long callerID;

    public InsertMessageQuery(Message message, long callerID) {
        this.message = message;
        this.callerID = callerID;
    }

    @Override
    public long getMessageID() {
        return message.getMessageID();
    }

    @Override
    public long getChatID() {
        return message.getChatID();
    }

    @Override
    public long getCallerID() {
        return callerID;
    }
}

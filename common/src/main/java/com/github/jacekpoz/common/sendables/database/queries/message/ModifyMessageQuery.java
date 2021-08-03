package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

public class ModifyMessageQuery implements MessageQuery {

    private final long messageID;
    private final long chatID;
    @Getter
    private final String columnToModify;
    @Getter
    private final String newValue;
    private final long callerID;

    public ModifyMessageQuery(long messageID, long chatID, String columnToModify, String newValue, long callerID) {
        this.messageID = messageID;
        this.chatID = chatID;
        this.columnToModify = columnToModify;
        this.newValue = newValue;
        this.callerID = callerID;
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

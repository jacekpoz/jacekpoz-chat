package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;
import lombok.Getter;

public class ModifyChatQuery implements ChatQuery {

    private final long chatID;
    @Getter
    private final String columnToModify;
    @Getter
    private final String newValue;
    private final long callerID;

    public ModifyChatQuery(long chatID, String columnToModify, String newValue, long callerID) {
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
    public long getCallerID() {
        return callerID;
    }
}

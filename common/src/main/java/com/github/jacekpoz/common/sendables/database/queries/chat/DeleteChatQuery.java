package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;

public class DeleteChatQuery implements ChatQuery {

    private final long chatID;
    private final long callerID;

    public DeleteChatQuery(long chatID, long callerID) {
        this.chatID = chatID;
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

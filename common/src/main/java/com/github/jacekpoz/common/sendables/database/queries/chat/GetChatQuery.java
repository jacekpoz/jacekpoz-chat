package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;

public class GetChatQuery implements ChatQuery {

    private final long chatID;
    private final long callerID;

    public GetChatQuery(long chatID, long callerID) {
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

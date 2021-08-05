package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;

public class GetChatQuery extends ChatQuery {

    public GetChatQuery(long chatID, long callerID) {
        super(chatID, callerID);
    }

    @Override
    public String toString() {
        return "GetChatQuery{" +
                "chatID=" + getChatID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

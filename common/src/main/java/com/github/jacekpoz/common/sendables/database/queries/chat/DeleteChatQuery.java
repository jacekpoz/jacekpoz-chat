package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;

public class DeleteChatQuery extends ChatQuery {

    public DeleteChatQuery(long chatID, long callerID) {
        super(chatID, callerID);
    }

    @Override
    public String toString() {
        return "DeleteChatQuery{" +
                "chatID=" + getChatID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

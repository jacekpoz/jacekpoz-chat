package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

public class GetUsersInChatQuery extends UserQuery {

    public GetUsersInChatQuery(long chatID, long callerID) {
        super(chatID, callerID);
    }

    public long getChatID() {
        return getUserID();
    }

    @Override
    public String toString() {
        return "GetUsersInChatQuery{" +
                "chatID=" + getChatID() +
                ", callerID=" + getCallerID() +
                '}';
    }
}

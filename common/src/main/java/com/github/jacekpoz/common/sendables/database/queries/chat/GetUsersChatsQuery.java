package com.github.jacekpoz.common.sendables.database.queries.chat;


public class GetUsersChatsQuery extends GetChatQuery {

    public GetUsersChatsQuery(long userID, long callerID) {
        super(userID, callerID);
    }

    public long getUserID() {
        return getChatID();
    }
}

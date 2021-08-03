package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.Screen;

public class GetUsersChatsQuery extends GetChatQuery {

    public GetUsersChatsQuery(long userID, long callerID) {
        super(userID, callerID);
    }

}

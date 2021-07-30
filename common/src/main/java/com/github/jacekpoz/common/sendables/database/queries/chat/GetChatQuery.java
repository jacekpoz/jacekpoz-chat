package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.GetQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;

public class GetChatQuery extends GetQuery<Chat> implements ChatQuery {

    protected GetChatQuery(long chatID, Screen caller) {
        super(chatID, caller);
    }

    @Override
    public long getChatID() {
        return typeID;
    }
}

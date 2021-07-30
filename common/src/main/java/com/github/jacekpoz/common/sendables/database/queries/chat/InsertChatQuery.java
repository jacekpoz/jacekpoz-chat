package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.InsertQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;

public class InsertChatQuery extends InsertQuery<Chat> implements ChatQuery {

    private final String chatName;

    public InsertChatQuery(String chatName, Screen caller) {
        super(caller);
        this.chatName = chatName;
    }

    @Override
    public long getChatID() {
        return -1;
    }
}

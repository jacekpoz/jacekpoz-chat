package com.github.jacekpoz.common.sendables.database.results;

import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatResult implements Result<Chat> {

    private final ChatQuery query;
    private final List<Chat> chats;

    public ChatResult(ChatQuery cq) {
        query = cq;
        chats = new ArrayList<>();
    }
    
    @Override
    public List<Chat> get() {
        return chats;
    }

    @Override
    public void add(Chat chat) {
        chats.add(chat);
    }

    @Override
    public Query<Chat> getQuery() {
        return query;
    }
}

package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;
import lombok.Getter;

import java.util.List;

public class InsertChatQuery extends ChatQuery {

    @Getter
    private final String chatName;
    @Getter
    private final List<User> members;

    public InsertChatQuery(String chatName, List<User> members, long callerID) {
        super(-1, callerID);
        this.chatName = chatName;
        this.members = members;
    }

    @Override
    public long getChatID() {
        return -1;
    }

}

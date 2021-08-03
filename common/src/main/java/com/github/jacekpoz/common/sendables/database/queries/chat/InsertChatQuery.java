package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;
import lombok.Getter;

import java.util.List;

public class InsertChatQuery implements ChatQuery {

    @Getter
    private final String chatName;
    @Getter
    private final List<User> members;
    private final long callerID;

    public InsertChatQuery(String chatName, List<User> members, long callerID) {
        this.chatName = chatName;
        this.members = members;
        this.callerID = callerID;
    }

    @Override
    public long getChatID() {
        return -1;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }
}

package com.github.jacekpoz.common.sendables.database.queries.interfaces;

import com.github.jacekpoz.common.sendables.Chat;
import lombok.Getter;

public abstract class ChatQuery implements Query<Chat> {

    @Getter
    private final long chatID;
    private final long callerID;

    public ChatQuery(long chatID, long callerID) {
        this.chatID = chatID;
        this.callerID = callerID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }

}

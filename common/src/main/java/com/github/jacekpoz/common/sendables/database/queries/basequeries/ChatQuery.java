package com.github.jacekpoz.common.sendables.database.queries.basequeries;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatQuery chatQuery = (ChatQuery) o;
        return chatID == chatQuery.chatID && callerID == chatQuery.callerID;
    }

    @Override
    public String toString() {
        return "ChatQuery{" +
                "chatID=" + chatID +
                ", callerID=" + callerID +
                '}';
    }
}

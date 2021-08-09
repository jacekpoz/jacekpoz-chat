package com.github.jacekpoz.common.sendables.database.queries.basequeries;

import com.github.jacekpoz.common.sendables.Message;
import lombok.Getter;


public abstract class MessageQuery implements Query<Message> {

    @Getter
    private final long messageID;
    @Getter
    private final long chatID;
    private final long callerID;

    public MessageQuery(long messageID, long chatID, long callerID) {
        this.messageID = messageID;
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
        MessageQuery that = (MessageQuery) o;
        return messageID == that.messageID && chatID == that.chatID && callerID == that.callerID;
    }

    @Override
    public String toString() {
        return "MessageQuery{" +
                "messageID=" + messageID +
                ", chatID=" + chatID +
                ", callerID=" + callerID +
                '}';
    }
}

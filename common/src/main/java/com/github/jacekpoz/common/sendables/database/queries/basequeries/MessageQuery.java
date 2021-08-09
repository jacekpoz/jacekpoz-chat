package com.github.jacekpoz.common.sendables.database.queries.basequeries;

import com.github.jacekpoz.common.sendables.Message;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
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

}

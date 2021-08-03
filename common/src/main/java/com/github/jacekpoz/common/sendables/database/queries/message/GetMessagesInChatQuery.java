package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

public class GetMessagesInChatQuery implements MessageQuery {

    private final long chatID;
    @Getter
    private final long offset;
    @Getter
    private final long limit;
    private final long callerID;

    public GetMessagesInChatQuery(long chatID, long offset, long limit, long callerID) {
        this.chatID = chatID;
        this.offset = offset;
        this.limit = limit;
        this.callerID = callerID;
    }

    public GetMessagesInChatQuery(long chatID, long limit, long callerID) {
        this(chatID, 0, limit, callerID);
    }

    public GetMessagesInChatQuery(long chatID, long callerID) {
        this(chatID, 0, Constants.DEFAULT_MESSAGES_LIMIT, callerID);
    }

    @Override
    public long getMessageID() {
        return -1;
    }

    @Override
    public long getChatID() {
        return chatID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }
}

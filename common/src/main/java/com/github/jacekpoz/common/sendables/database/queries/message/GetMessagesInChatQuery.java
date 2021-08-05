package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

public class GetMessagesInChatQuery extends MessageQuery {

    @Getter
    private final long offset;
    @Getter
    private final long limit;

    public GetMessagesInChatQuery(long chatID, long offset, long limit, long callerID) {
        super(-1, chatID, callerID);
        this.offset = offset;
        this.limit = limit;
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

}

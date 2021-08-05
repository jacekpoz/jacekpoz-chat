package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;

public class DeleteMessageQuery extends MessageQuery {

    public DeleteMessageQuery(long messageID, long chatID, long callerID) {
        super(messageID, chatID, callerID);
    }

    public DeleteMessageQuery(long chatID, long callerID) {
        this(-1, chatID, callerID);
    }

}

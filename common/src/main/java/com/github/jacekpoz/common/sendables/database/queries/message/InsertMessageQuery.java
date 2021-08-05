package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

public class InsertMessageQuery extends MessageQuery {

    @Getter
    private final long authorID;
    @Getter
    private final String content;

    public InsertMessageQuery(long messageID, long chatID, long authorID, String content, long callerID) {
        super(messageID, chatID, callerID);
        this.authorID = authorID;
        this.content = content;
    }

}

package com.github.jacekpoz.common.sendables.database.queries.user;

import lombok.Getter;

public class GetMessageAuthorQuery extends GetUserQuery {

    @Getter
    private final long messageID;

    public GetMessageAuthorQuery(long messageID, long authorID, long callerID) {
        super(authorID, callerID);
        this.messageID = messageID;
    }
}

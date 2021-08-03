package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Message;
import lombok.Getter;

public class GetMessageAuthorQuery extends GetUserQuery {

    @Getter
    private final Message message;

    public GetMessageAuthorQuery(Message message, long callerID) {
        super(message.getAuthorID(), callerID);
        this.message = message;
    }
}

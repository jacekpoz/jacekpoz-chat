package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

public class ModifyMessageQuery extends MessageQuery {

    @Getter
    private final String columnToModify;
    @Getter
    private final String newValue;

    public ModifyMessageQuery(long messageID, long chatID, String columnToModify, String newValue, long callerID) {
        super(messageID, chatID, callerID);
        this.columnToModify = columnToModify;
        this.newValue = newValue;
    }

}

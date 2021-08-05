package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;
import lombok.Getter;

public class ModifyChatQuery extends ChatQuery {

    @Getter
    private final String columnToModify;
    @Getter
    private final String newValue;

    public ModifyChatQuery(long chatID, String columnToModify, String newValue, long callerID) {
        super(chatID, callerID);
        this.columnToModify = columnToModify;
        this.newValue = newValue;
    }

}

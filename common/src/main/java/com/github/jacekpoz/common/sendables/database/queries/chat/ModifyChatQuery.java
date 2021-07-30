package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.ModifyQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;

public class ModifyChatQuery extends ModifyQuery<Chat> implements ChatQuery {

    public ModifyChatQuery(long id, String columnToModify, String newValue, Screen caller) {
        super(id, columnToModify, newValue, caller);
    }

    @Override
    public long getChatID() {
        return typeID;
    }

}

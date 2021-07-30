package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.ModifyQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;

public class ModifyMessageQuery extends ModifyQuery<Message> implements MessageQuery {

    private final long chatID;

    public ModifyMessageQuery(long messageID, long chatID, String columnToModify, String newValue, Screen caller) {
        super(messageID, columnToModify, newValue, caller);
        this.chatID = chatID;
    }

    @Override
    public long getChatID() {
        return chatID;
    }

    public long getMessageID() {
        return typeID;
    }
}

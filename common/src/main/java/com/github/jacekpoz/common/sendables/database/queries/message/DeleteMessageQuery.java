package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.DeleteQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;

public class DeleteMessageQuery extends DeleteQuery<Message> implements MessageQuery {

    private final long chatID;

    public DeleteMessageQuery(long messageID, long chatID, Screen caller) {
        super(messageID, caller);
        this.chatID = chatID;
    }

    public DeleteMessageQuery(long chatID, Screen caller) {
        this(-1, chatID, caller);
    }

    @Override
    public long getChatID() {
        return chatID;
    }

    public long getMessageID() {
        return typeID;
    }
}

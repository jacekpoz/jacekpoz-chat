package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.InsertQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

public class InsertMessageQuery extends InsertQuery<Message> implements MessageQuery {

    private final long chatID;
    @Getter
    private final long authorID;
    @Getter
    private final String content;

    public InsertMessageQuery(long chatID, long authorID, String content, Screen caller) {
        super(caller);
        this.chatID = chatID;
        this.authorID = authorID;
        this.content = content;
    }

    @Override
    public long getChatID() {
        return chatID;
    }
}

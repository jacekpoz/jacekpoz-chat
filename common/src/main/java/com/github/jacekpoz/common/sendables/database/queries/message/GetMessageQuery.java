package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.database.queries.abstracts.GetQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

public class GetMessageQuery extends GetQuery<Message> implements MessageQuery {

    @Getter
    private final long offset;
    @Getter
    private final long limit;

    public GetMessageQuery(long chatID, long offset, long limit, Screen caller) {
        super(chatID, caller);
        this.offset = offset;
        this.limit = limit;
    }

    public GetMessageQuery(long chatID, long limit, Screen caller) {
        this(chatID, 0, limit, caller);
    }

    public GetMessageQuery(long chatID, Screen caller) {
        this(chatID, 0, Constants.DEFAULT_MESSAGES_LIMIT, caller);
    }

    @Override
    public long getChatID() {
        return typeID;
    }
}

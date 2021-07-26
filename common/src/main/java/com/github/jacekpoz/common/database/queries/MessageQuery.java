package com.github.jacekpoz.common.database.queries;

import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.Message;
import lombok.Getter;

import java.util.Optional;

public class MessageQuery implements Query<Message> {

    @Getter
    private final long chatID;
    @Getter
    private final long messageLimit;
    @Getter
    private final long offset;
    @Getter
    private Optional<Long> authorID;

    public MessageQuery(long cID) {
        this(cID, Constants.DEFAULT_MESSAGES_LIMIT);
    }

    public MessageQuery(long cID, long limit) {
        this(cID, limit, 0);
    }

    public MessageQuery(long cID, long limit, long off) {
        chatID = cID;
        messageLimit = limit;
        offset = off;
        authorID = Optional.empty();
    }

    public MessageQuery(long cID, long limit, long off, long aID) {
        this(cID, limit, off);
        authorID = Optional.of(aID);
    }
}

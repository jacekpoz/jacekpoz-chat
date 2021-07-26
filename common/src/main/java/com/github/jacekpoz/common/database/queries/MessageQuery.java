package com.github.jacekpoz.common.database.queries;

import com.github.jacekpoz.common.Constants;
import com.github.jacekpoz.common.Message;

import java.util.Optional;

public class MessageQuery implements Query<Message> {

    private final long chatID;
    private final long messageLimit;
    private final long offset;
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

    @Override
    public String getQuery() {
        return "SELECT * FROM " +
                Constants.MESSAGES_TABLE +
                " WHERE chat_id = " + chatID +
                (authorID.map(aLong -> " AND authorID = " + aLong).orElse("")) +
                " LIMIT " + offset + ", " + messageLimit + ";";
    }
}

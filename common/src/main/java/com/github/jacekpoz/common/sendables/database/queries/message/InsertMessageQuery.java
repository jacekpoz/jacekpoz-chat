package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

import java.util.Objects;

public class InsertMessageQuery extends MessageQuery {

    @Getter
    private final long authorID;
    @Getter
    private final String content;

    public InsertMessageQuery(long messageID, long chatID, long authorID, String content, long callerID) {
        super(messageID, chatID, callerID);
        this.authorID = authorID;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InsertMessageQuery)) return false;
        if (!super.equals(o)) return false;
        InsertMessageQuery that = (InsertMessageQuery) o;
        return getMessageID() == that.getMessageID() &&
                getChatID() == that.getChatID() &&
                authorID == that.authorID &&
                Objects.equals(content, that.content) &&
                getAuthorID() == that.getAuthorID();
    }

    @Override
    public String toString() {
        return "InsertMessageQuery{" +
                "messageID=" + getMessageID() +
                ", chatID=" + getChatID() +
                ", authorID=" + authorID +
                ", content='" + content + '\'' +
                ", callerID=" + getCallerID() +
                '}';
    }
}

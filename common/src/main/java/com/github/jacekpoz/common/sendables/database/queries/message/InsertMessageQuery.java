package com.github.jacekpoz.common.sendables.database.queries.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.MessageQuery;
import lombok.Getter;

import java.util.Objects;

public class InsertMessageQuery extends MessageQuery {

    @Getter
    private final long authorID;
    @Getter
    private final String content;

    @JsonCreator
    public InsertMessageQuery(
            @JsonProperty("messageID") long messageID,
            @JsonProperty("chatID") long chatID,
            @JsonProperty("authorID") long authorID,
            @JsonProperty("content") String content,
            @JsonProperty("callerID") long callerID
    ) {
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

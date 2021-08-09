package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.ChatQuery;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

public class InsertChatQuery extends ChatQuery {

    @Getter
    private final String chatName;
    @Getter
    private final List<Long> memberIDs;

    @JsonCreator
    public InsertChatQuery(
            @JsonProperty("chatName") String chatName,
            @JsonProperty("memberIDs") List<Long> memberIDs,
            @JsonProperty("callerIDs") long callerID
    ) {
        super(-1, callerID);
        this.chatName = chatName;
        this.memberIDs = memberIDs;
    }

    @Override
    public long getChatID() {
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InsertChatQuery)) return false;
        if (!super.equals(o)) return false;
        InsertChatQuery that = (InsertChatQuery) o;
        return getChatID() == that.getChatID() &&
                Objects.equals(chatName, that.chatName) &&
                Objects.equals(memberIDs, that.memberIDs) &&
                getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "InsertChatQuery{" +
                "chatName='" + chatName + '\'' +
                ", memberIDs=" + memberIDs +
                ", callerID=" + getCallerID() +
                '}';
    }
}

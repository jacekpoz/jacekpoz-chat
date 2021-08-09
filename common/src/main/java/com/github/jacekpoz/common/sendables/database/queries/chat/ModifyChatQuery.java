package com.github.jacekpoz.common.sendables.database.queries.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.ChatQuery;
import lombok.Getter;

import java.util.Objects;

public class ModifyChatQuery extends ChatQuery {

    @Getter
    private final String columnToModify;
    @Getter
    private final String newValue;

    @JsonCreator
    public ModifyChatQuery(
            @JsonProperty("chatID") long chatID,
            @JsonProperty("columnToModify") String columnToModify,
            @JsonProperty("newValue") String newValue,
            @JsonProperty("callerID") long callerID
    ) {
        super(chatID, callerID);
        this.columnToModify = columnToModify;
        this.newValue = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModifyChatQuery)) return false;
        if (!super.equals(o)) return false;
        ModifyChatQuery that = (ModifyChatQuery) o;
        return getChatID() == that.getChatID() &&
                Objects.equals(columnToModify, that.columnToModify) &&
                Objects.equals(newValue, that.newValue) &&
                getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "ModifyChatQuery{" +
                "chatID=" + getChatID() +
                ", columnToModify='" + columnToModify + '\'' +
                ", newValue='" + newValue + '\'' +
                ", callerID=" + getCallerID() +
                '}';
    }
}

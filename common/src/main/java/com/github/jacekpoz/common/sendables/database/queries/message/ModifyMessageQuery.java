package com.github.jacekpoz.common.sendables.database.queries.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.MessageQuery;
import lombok.Getter;

import java.util.Objects;

public class ModifyMessageQuery extends MessageQuery {

    @Getter
    private final String columnToModify;
    @Getter
    private final String newValue;

    @JsonCreator
    public ModifyMessageQuery(
            @JsonProperty("messageID") long messageID,
            @JsonProperty("chatID") long chatID,
            @JsonProperty("columnToModify") String columnToModify,
            @JsonProperty("newValue") String newValue,
            @JsonProperty("callerID") long callerID
    ) {
        super(messageID, chatID, callerID);
        this.columnToModify = columnToModify;
        this.newValue = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModifyMessageQuery)) return false;
        if (!super.equals(o)) return false;
        ModifyMessageQuery that = (ModifyMessageQuery) o;
        return getMessageID() == that.getMessageID() &&
                getChatID() == that.getChatID() &&
                Objects.equals(columnToModify, that.columnToModify) &&
                Objects.equals(newValue, that.newValue) &&
                getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "ModifyMessageQuery{" +
                "messageID=" + getMessageID() +
                ", chatID=" + getChatID() +
                ", columnToModify='" + columnToModify + '\'' +
                ", newValue='" + newValue + '\'' +
                ", callerID=" + getCallerID() +
                '}';
    }
}

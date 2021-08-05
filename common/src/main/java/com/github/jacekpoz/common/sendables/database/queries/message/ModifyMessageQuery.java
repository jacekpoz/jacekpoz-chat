package com.github.jacekpoz.common.sendables.database.queries.message;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import lombok.Getter;

import java.util.Objects;

public class ModifyMessageQuery extends MessageQuery {

    @Getter
    private final String columnToModify;
    @Getter
    private final String newValue;

    public ModifyMessageQuery(long messageID, long chatID, String columnToModify, String newValue, long callerID) {
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

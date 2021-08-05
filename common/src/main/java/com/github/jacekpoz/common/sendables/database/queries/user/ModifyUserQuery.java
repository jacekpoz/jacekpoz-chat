package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

import java.util.Objects;

public class ModifyUserQuery extends UserQuery {

    @Getter
    private final String columnToModify;
    @Getter
    private final String newValue;

    public ModifyUserQuery(long userID, String columnToModify, String newValue, long callerID) {
        super(userID, callerID);
        this.columnToModify = columnToModify;
        this.newValue = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModifyUserQuery)) return false;
        if (!super.equals(o)) return false;
        ModifyUserQuery that = (ModifyUserQuery) o;
        return getUserID() == that.getUserID() &&
                Objects.equals(columnToModify, that.columnToModify) &&
                Objects.equals(newValue, that.newValue) &&
                getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "ModifyUserQuery{" +
                "userID=" + getUserID() +
                ", columnToModify='" + columnToModify + '\'' +
                ", newValue='" + newValue + '\'' +
                ", callerID=" + getCallerID() +
                '}';
    }
}

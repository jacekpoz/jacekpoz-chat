package com.github.jacekpoz.common.sendables.database.queries.user;

import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;

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

}

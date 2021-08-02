package com.github.jacekpoz.common.sendables.database.queries.abstracts;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.Query;
import lombok.Getter;

public abstract class ModifyQuery<T extends Sendable> implements Query<T> {

    protected final long typeID;
    @Getter
    protected final String columnToModify;
    @Getter
    protected final String newValue;

    protected final Screen caller;

    public ModifyQuery(long typeID, String columnToModify, String newValue, Screen caller) {
        this.typeID = typeID;
        this.columnToModify = columnToModify;
        this.newValue = newValue;
        this.caller = caller;
    }

    @Override
    public Screen getCaller() {
        return caller;
    }

}

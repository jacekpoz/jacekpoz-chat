package com.github.jacekpoz.common.sendables.database.queries.abstracts;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.Query;
import lombok.Getter;

public abstract class GetQuery<T extends Sendable> implements Query<T> {

    @Getter
    protected final long typeID;

    protected final Screen caller;

    protected GetQuery(long typeID, Screen caller) {
        this.typeID = typeID;
        this.caller = caller;
    }

    @Override
    public Screen getCaller() {
        return caller;
    }
}

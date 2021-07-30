package com.github.jacekpoz.common.sendables.database.queries.abstracts;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.Query;

public abstract class InsertQuery<T extends Sendable> implements Query<T> {

    protected final Screen caller;

    public InsertQuery(Screen caller) {
        this.caller = caller;
    }

    @Override
    public Screen getCaller() {
        return caller;
    }
}

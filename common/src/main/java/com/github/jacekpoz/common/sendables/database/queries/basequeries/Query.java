package com.github.jacekpoz.common.sendables.database.queries.basequeries;

import com.github.jacekpoz.common.sendables.Sendable;

public interface Query<T> extends Sendable {

    long getCallerID();

}

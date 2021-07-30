package com.github.jacekpoz.common.sendables.database.queries.interfaces;

import com.github.jacekpoz.common.Screen;
import com.github.jacekpoz.common.sendables.Sendable;

public interface Query<T> extends Sendable {

    Screen getCaller();

}

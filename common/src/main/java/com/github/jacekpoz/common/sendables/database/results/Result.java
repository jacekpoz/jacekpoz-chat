package com.github.jacekpoz.common.sendables.database.results;

import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.Query;

import java.util.List;

public interface Result<T extends Sendable> extends Sendable {

    List<T> get();

    void add(T t);

    Query<T> getQuery();

}

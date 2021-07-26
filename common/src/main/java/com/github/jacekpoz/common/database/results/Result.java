package com.github.jacekpoz.common.database.results;

import com.github.jacekpoz.common.Sendable;

import java.util.List;

public interface Result<T> extends Sendable {

    List<T> get();

    void add(T t);

}

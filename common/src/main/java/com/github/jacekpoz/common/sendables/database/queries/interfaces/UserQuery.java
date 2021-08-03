package com.github.jacekpoz.common.sendables.database.queries.interfaces;

import com.github.jacekpoz.common.sendables.User;

public interface UserQuery extends Query<User> {

    long getUserID();
}

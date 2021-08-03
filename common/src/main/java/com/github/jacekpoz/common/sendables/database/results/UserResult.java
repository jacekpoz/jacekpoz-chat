package com.github.jacekpoz.common.sendables.database.results;

import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.Query;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;

import java.util.ArrayList;
import java.util.List;

public class UserResult implements Result<User> {

    private final UserQuery query;
    private final List<User> users;
    private boolean success;

    public UserResult(UserQuery uq) {
        query = uq;
        users = new ArrayList<>();
    }

    @Override
    public List<User> get() {
        return users;
    }

    @Override
    public void add(User user) {
        users.add(user);
    }

    @Override
    public void add(List<User> users) {
        this.users.addAll(users);
    }

    @Override
    public Query<User> getQuery() {
        return query;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean success() {
        return success;
    }
}

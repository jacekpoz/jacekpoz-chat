package com.github.jacekpoz.common.database.results;

import com.github.jacekpoz.common.User;

import java.util.ArrayList;
import java.util.List;

public class UserResult implements Result<User> {

    private final List<User> users;

    public UserResult() {
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
}

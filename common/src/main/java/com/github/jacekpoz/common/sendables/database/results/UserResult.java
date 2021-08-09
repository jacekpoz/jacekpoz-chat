package com.github.jacekpoz.common.sendables.database.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.Query;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserResult implements Result<User> {

    private final UserQuery query;
    @JsonProperty("users")
    private final List<User> users;
    @JsonProperty("success")
    private boolean success;

    @JsonCreator
    public UserResult(
            @JsonProperty("query") UserQuery uq
    ) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResult that = (UserResult) o;
        return success == that.success && Objects.equals(query, that.query) && Objects.equals(users, that.users);
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "query=" + query +
                ", users=" + users +
                ", success=" + success +
                '}';
    }
}

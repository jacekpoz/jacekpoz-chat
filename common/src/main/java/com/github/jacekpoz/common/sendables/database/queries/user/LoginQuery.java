package com.github.jacekpoz.common.sendables.database.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

public class LoginQuery extends GetUserQuery {

    @Getter
    private final byte[] password;

    @JsonCreator
    public LoginQuery(
            @JsonProperty("username") String username,
            @JsonProperty("password") byte[] password,
            @JsonProperty("callerID") long callerID
    ) {
        super(username, callerID);
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginQuery)) return false;
        if (!super.equals(o)) return false;
        LoginQuery that = (LoginQuery) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Arrays.equals(password, that.password) &&
                getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "LoginQuery{" +
                "username='" + getUsername() + '\'' +
                ", password=" + Arrays.toString(password) +
                ", callerID=" + getCallerID() +
                '}';
    }
}

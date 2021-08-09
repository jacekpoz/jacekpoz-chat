package com.github.jacekpoz.common.sendables.database.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.EnumResults;
import com.github.jacekpoz.common.sendables.database.queries.user.LoginQuery;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;

public class LoginResult extends UserResult {

    @Getter
    @Setter
    private EnumResults.Login result;

    @Getter
    @Setter
    private SQLException ex;

    @JsonCreator
    public LoginResult(
            @JsonProperty("query") LoginQuery lq
    ) {
        super(lq);
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "query=" + getQuery() +
                ", users=" + get() +
                ", success=" + success() +
                ", result=" + result +
                ", ex=" + ex +
                '}';
    }
}

package com.github.jacekpoz.common.sendables.database.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.EnumResults;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;

public class RegisterResult extends UserResult {
    @Getter
    @Setter
    private EnumResults.Register result;

    @Getter
    @Setter
    private SQLException ex;

    @JsonCreator
    public RegisterResult(
            @JsonProperty("query") UserQuery uq
    ) {
        super(uq);
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "query=" + getQuery() +
                ", users=" + get() +
                ", success=" + success() +
                ", result=" + result +
                ", ex=" + ex +
                '}';
    }
}

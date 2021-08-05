package com.github.jacekpoz.common.sendables.database.results;

import com.github.jacekpoz.common.sendables.database.EnumResults;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
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

    public RegisterResult(UserQuery uq) {
        super(uq);
    }
}

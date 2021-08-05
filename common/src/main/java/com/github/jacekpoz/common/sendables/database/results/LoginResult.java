package com.github.jacekpoz.common.sendables.database.results;

import com.github.jacekpoz.common.sendables.database.EnumResults;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
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

    public LoginResult(LoginQuery lq) {
        super(lq);
    }
}

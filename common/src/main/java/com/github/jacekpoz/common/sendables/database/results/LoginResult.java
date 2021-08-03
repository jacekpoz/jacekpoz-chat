package com.github.jacekpoz.common.sendables.database.results;

import com.github.jacekpoz.common.sendables.database.EnumResults;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;
import lombok.Setter;

public class LoginResult extends UserResult {

    @Getter
    @Setter
    private EnumResults.LoginResult result;

    public LoginResult(UserQuery uq) {
        super(uq);
    }
}

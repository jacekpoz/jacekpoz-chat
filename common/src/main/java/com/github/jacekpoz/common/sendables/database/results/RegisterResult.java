package com.github.jacekpoz.common.sendables.database.results;

import com.github.jacekpoz.common.sendables.database.EnumResults;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import lombok.Getter;
import lombok.Setter;

public class RegisterResult extends UserResult {
    @Getter
    @Setter
    private EnumResults.RegisterResult result;

    public RegisterResult(UserQuery uq) {
        super(uq);
    }
}

package com.github.jacekpoz.common.sendables.database.queries.basequeries;

import com.github.jacekpoz.common.sendables.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class UserQuery implements Query<User> {

    @Getter
    private final long userID;
    private final long callerID;

    public UserQuery(long userID, long callerID) {
        this.userID = userID;
        this.callerID = callerID;
    }

    @Override
    public long getCallerID() {
        return callerID;
    }

}

package com.github.jacekpoz.common.sendables.database.queries.user;

import lombok.Getter;

public class GetMessageAuthorQuery extends GetUserQuery {

    @Getter
    private final long messageID;

    public GetMessageAuthorQuery(long messageID, long authorID, long callerID) {
        super(authorID, callerID);
        this.messageID = messageID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetMessageAuthorQuery)) return false;
        if (!super.equals(o)) return false;
        GetMessageAuthorQuery that = (GetMessageAuthorQuery) o;
        return getUserID() == that.getUserID() &&
                messageID == that.messageID &&
                getCallerID() == that.getCallerID();
    }

    @Override
    public String toString() {
        return "GetMessageAuthorQuery{" +
                "authorID=" + getUserID() +
                ", messageID=" + messageID +
                ", callerID=" + getCallerID() +
                '}';
    }
}

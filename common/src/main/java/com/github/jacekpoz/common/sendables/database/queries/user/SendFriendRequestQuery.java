package com.github.jacekpoz.common.sendables.database.queries.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jacekpoz.common.sendables.database.queries.basequeries.UserQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class SendFriendRequestQuery extends UserQuery {

    @Getter
    private final long friendID;

    @JsonCreator
    public SendFriendRequestQuery(
            @JsonProperty("senderID") long senderID,
            @JsonProperty("friendID") long friendID,
            @JsonProperty("callerID") long callerID
    ) {
        super(senderID, callerID);
        this.friendID = friendID;
    }

}

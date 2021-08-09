package com.github.jacekpoz.common.sendables;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat implements Sendable {
    @ToString.Include
    @EqualsAndHashCode.Include
    @Getter
    private final long id;
    @ToString.Include
    @Getter @Setter
    private String name;
    @Getter
    private final List<Long> memberIDs;
    @Getter
    private final List<Message> messages;
    @Getter
    private final LocalDateTime dateCreated;
    @Getter
    private final long messageCounter;

    @JsonCreator
    public Chat(
            @JsonProperty("chatID") long chatID,
            @JsonProperty("chatName") String chatName,
            @JsonProperty("dateCreated") LocalDateTime created,
            @JsonProperty("messageCounter") long mCounter
    ) {
        id = chatID;
        name = chatName;
        memberIDs = new ArrayList<>();
        messages = new ArrayList<>();
        dateCreated = created;
        messageCounter = mCounter;
    }

    public void sortMessages() {
        messages.sort(Comparator.comparingLong(Message::getMessageID));
    }

}

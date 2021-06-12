package com.github.jacekpoz.common;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Conversation implements Serializable {
    private static final long serialVersionUID = -8973712096190274407L;
    private @Getter long id;
    private @Getter List<UserInfo> members;
    private @Getter List<Message> messages;
    private @Getter long messageCounter;

    public Conversation(long chatID) {
        id = chatID;
    }

    public void addUser(UserInfo u) {
        if (!members.contains(u)) members.add(u);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return id == that.id && messageCounter == that.messageCounter && Objects.equals(members, that.members) && Objects.equals(messages, that.messages);
    }
}

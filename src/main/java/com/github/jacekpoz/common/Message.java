package com.github.jacekpoz.common;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Message implements Serializable {

    private static final long serialVersionUID = -3347591867787345026L;
    private final @Getter
    long messageID;
    private final @Getter
    long chatID;
    private @Getter @Setter
    String content;
    private final @Getter
    Timestamp sendDate;
    private final @Getter
    long authorID;

    public Message(long mID, long cID, long aID, String text, Timestamp send) {
        messageID = mID;
        chatID = cID;
        authorID = aID;
        content = text;
        sendDate = send;
    }

    public Message(String text) {
        this(-1, -1, -1, text,
                Timestamp.valueOf(LocalDateTime.MIN)
        );
    }

    /*
     * auto generated by intellij
     * **************************
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return messageID == message.messageID && chatID == message.chatID;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageID=" + messageID +
                ", chatID=" + chatID +
                ", content='" + content + "'" +
                ", sendDate=" + sendDate +
                ", author=" + authorID +
                "}";
    }
    /*
     * **************************
     */
}

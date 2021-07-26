package com.github.jacekpoz.common.database.results;

import com.github.jacekpoz.common.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageResult implements Result<Message> {

    private final List<Message> messages;

    public MessageResult() {
        messages = new ArrayList<>();
    }

    @Override
    public List<Message> get() {
        return messages;
    }

    @Override
    public void add(Message message) {
        messages.add(message);
    }
}

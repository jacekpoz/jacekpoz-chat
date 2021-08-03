package com.github.jacekpoz.common.sendables.database.results;

import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.Query;

import java.util.ArrayList;
import java.util.List;

public class MessageResult implements Result<Message> {

    private final MessageQuery query;
    private final List<Message> messages;
    private boolean success;

    public MessageResult(MessageQuery mq) {
        query = mq;
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

    @Override
    public void add(List<Message> messages) {
        this.messages.addAll(messages);
    }

    @Override
    public Query<Message> getQuery() {
        return query;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean success() {
        return success;
    }
}

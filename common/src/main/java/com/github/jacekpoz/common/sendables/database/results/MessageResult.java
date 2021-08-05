package com.github.jacekpoz.common.sendables.database.results;

import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageResult that = (MessageResult) o;
        return success == that.success && Objects.equals(query, that.query) && Objects.equals(messages, that.messages);
    }

    @Override
    public String toString() {
        return "MessageResult{" +
                "query=" + query +
                ", messages=" + messages +
                ", success=" + success +
                '}';
    }
}

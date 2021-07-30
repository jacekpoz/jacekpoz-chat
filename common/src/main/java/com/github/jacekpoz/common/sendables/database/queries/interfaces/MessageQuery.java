package com.github.jacekpoz.common.sendables.database.queries.interfaces;

import com.github.jacekpoz.common.sendables.Message;

public interface MessageQuery extends Query<Message> {

    long getChatID();

}

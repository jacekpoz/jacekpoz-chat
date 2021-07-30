package com.github.jacekpoz.common.sendables.database.queries.interfaces;

import com.github.jacekpoz.common.sendables.Chat;

public interface ChatQuery extends Query<Chat> {

    long getChatID();

}

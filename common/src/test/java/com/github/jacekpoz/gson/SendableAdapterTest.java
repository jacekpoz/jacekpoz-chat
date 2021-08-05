package com.github.jacekpoz.gson;

import com.github.jacekpoz.common.gson.LocalDateTimeAdapter;
import com.github.jacekpoz.common.gson.SendableAdapter;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.queries.chat.GetUsersChatsQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.ChatQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.MessageQuery;
import com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery;
import com.github.jacekpoz.common.sendables.database.queries.message.InsertMessageQuery;
import com.github.jacekpoz.common.sendables.database.queries.user.GetUserQuery;
import com.github.jacekpoz.common.sendables.database.results.ChatResult;
import com.github.jacekpoz.common.sendables.database.results.MessageResult;
import com.github.jacekpoz.common.sendables.database.results.UserResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class SendableAdapterTest {

    @Test
    public void shouldSerializeAndDeserializeSendables() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Sendable.class, new SendableAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        Sendable[] input = {
                new Message("dupa"),
                new User(-1, "dupa", "dupa dupa", LocalDateTime.MIN),
                new Chat(-1, "dupa", LocalDateTime.MIN, -1),
                new InsertMessageQuery(-1, -1, -1, "dupa", -1),
                new GetUserQuery(-1, -1),
                new GetUsersChatsQuery(-1, -1),
                new ChatResult(new ChatQuery()),
                new MessageResult(new MessageQuery()),
                new UserResult(new UserQuery()),
        };

        for (Sendable s : input) {
            String sendableJson = gson.toJson(s, Sendable.class);
            System.out.println(sendableJson);
            Sendable sendable = gson.fromJson(sendableJson, Sendable.class);
            assertEquals(s, sendable);
        }
    }
}

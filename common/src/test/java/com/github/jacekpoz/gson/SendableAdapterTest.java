package com.github.jacekpoz.gson;

import com.github.jacekpoz.common.gson.SendableAdapter;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
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
                .create();

        Sendable[] input = {
                new Message("dupa"),
                new User(-1, "dupa", "dupa dupa", Timestamp.valueOf(LocalDateTime.MIN)),
                new Chat(-1, "dupa", Timestamp.valueOf(LocalDateTime.MIN), -1),
        };

        for (Sendable s : input) {
            String sendableJson = gson.toJson(s, Sendable.class);
            System.out.println(sendableJson);
            Sendable sendable = gson.fromJson(sendableJson, Sendable.class);
            assertEquals(s, sendable);
        }
    }
}

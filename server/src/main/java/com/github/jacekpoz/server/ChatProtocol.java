package com.github.jacekpoz.server;

import com.github.jacekpoz.common.Chat;
import com.github.jacekpoz.common.Message;
import com.github.jacekpoz.common.Sendable;
import com.github.jacekpoz.common.User;
import com.google.gson.Gson;

import java.io.IOException;

public class ChatProtocol {

    private final ChatWorker worker;
    private final Gson gson;

    public ChatProtocol(ChatWorker w) {
        worker = w;
        gson = new Gson();
    }

    public void processInput(String json) throws IOException {
        Sendable input = gson.fromJson(json, Sendable.class);
        System.out.println("input: " + input);

        if (input instanceof User) worker.setCurrentUser((User) input);
        if (input instanceof Chat) worker.setCurrentChat((Chat) input);

        if (input instanceof Message) {
            Message output = (Message) input;
            for (ChatWorker ct : worker.getServer().getThreads())
                if (worker.getCurrentChat().getMembers().contains(ct.getCurrentUser()))
                    ct.send(gson.toJson(output, output.getClass()));
        }
    }
}

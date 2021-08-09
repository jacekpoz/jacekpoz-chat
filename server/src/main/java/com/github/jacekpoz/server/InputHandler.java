package com.github.jacekpoz.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.jacekpoz.common.exceptions.UnknownSendableException;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;

import java.io.IOException;

public class InputHandler {

    private final ChatWorker worker;

    public InputHandler(ChatWorker w) {
        worker = w;
    }

    public void handleInput(Sendable input) throws IOException {
        if (input instanceof User) handleUser((User) input);
        if (input instanceof Chat) handleChat((Chat) input);
        if (input instanceof Message) handleMessage((Message) input);

        throw new UnknownSendableException(input);
    }

    private void handleUser(User u) {
        worker.setCurrentUser(u);
    }

    private void handleChat(Chat c) {
        worker.setCurrentChat(c);
    }

    private void handleMessage(Message m) {
        try {
            for (ChatWorker ct : worker.getServer().getThreads())
                if (worker.getCurrentChat().getMemberIDs().contains(ct.getCurrentUser().getId()))
                    ct.send(worker.getMapper().writeValueAsString(m));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

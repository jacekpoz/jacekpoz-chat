package com.github.jacekpoz.server;

import com.github.jacekpoz.common.Chat;
import com.github.jacekpoz.common.Message;
import com.github.jacekpoz.common.User;

import java.io.IOException;

public class ChatProtocol {

    private final ChatWorker worker;

    public ChatProtocol(ChatWorker w) {
        worker = w;
    }

    public void processInput(Object input) throws IOException {
        if (input instanceof User) worker.setCurrentUser((User) input);
        if (input instanceof Chat) worker.setCurrentChat((Chat) input);

        if (input instanceof Message) {
            Message output = (Message) input;
            for (ChatWorker ct : worker.getServer().getThreads())
                if (worker.getCurrentChat().getMembers().contains(ct.getCurrentUser()))
                    ct.send(output);
        }
    }
}

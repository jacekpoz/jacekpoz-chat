package com.github.jacekpoz.client;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.results.Result;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InputHandler {

    private final ChatWindow window;

    public InputHandler(ChatWindow w) {
        window = w;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() {
        ExecutorService service = Executors.newCachedThreadPool();

        service.submit(() -> {
            while (true) {
                try {
                    Sendable input = window.getGson().fromJson(window.getIn().readLine(), Sendable.class);
                    handleSendable(input);
                } catch (EOFException e) {
                    System.err.println("EOF");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleSendable(Sendable input) {
        if (input instanceof Message) {
            window.getMessageScreen().handleSendable(input);
        } else if (input instanceof User) {
            window.getClient().setUser((User) input);
        } else if (input instanceof Chat) {
            window.getClient().setChat((Chat) input);
        } else if (input instanceof Result) {
            Result<?> r = (Result<?>) input;
            long screenID = r.getQuery().getCallerID();
            window.getScreen(screenID).handleSendable(input);
        }

    }

}

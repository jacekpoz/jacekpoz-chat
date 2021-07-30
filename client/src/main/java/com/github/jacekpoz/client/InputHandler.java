package com.github.jacekpoz.client;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.database.results.MessageResult;
import com.github.jacekpoz.common.sendables.database.results.Result;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InputHandler {

    private final ChatWindow window;

    public InputHandler(ChatWindow w) {
        window = w;
    }

    public void start() {
        ExecutorService service = Executors.newCachedThreadPool();

        service.submit(() -> {
            while (true) {
                try {
                    Sendable input = window.getGson().fromJson(window.getIn().readLine(), Sendable.class);

                } catch (EOFException e) {
                    System.err.println("EOF");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public List handleResult(Result result) {

    }

    private List<Message> handleMessageResult(MessageResult mr) {
        return mr.get();
    }

    public void handleInput(Sendable input) {

    }

}

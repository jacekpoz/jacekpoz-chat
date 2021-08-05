package com.github.jacekpoz.client;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.sendables.Chat;
import com.github.jacekpoz.common.sendables.Message;
import com.github.jacekpoz.common.sendables.Sendable;
import com.github.jacekpoz.common.sendables.User;
import com.github.jacekpoz.common.sendables.database.results.Result;

import java.io.IOException;
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
            String inputJSON;
            try {
                while ((inputJSON = window.getIn().readLine()) != null) {
                    System.out.println(inputJSON);
                    /* TODO
                    java.lang.RuntimeException: Unable to invoke no-args constructor for interface
                    com.github.jacekpoz.common.sendables.database.queries.interfaces.UserQuery.
                    Registering an InstanceCreator with Gson for this type may fix this problem.

                    this motherfucker and also add logging (probably just plain java logger or log4j
                     */
                    Sendable input = window.getGson().fromJson(inputJSON, Sendable.class);
                    System.out.println(input);
                    handleSendable(input);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    private void handleSendable(Sendable input) {
        System.out.println(input.getClass().getSimpleName());
        if (input instanceof Message) {
            window.getMessageScreen().handleSendable(input);
        } else if (input instanceof User) {
            window.getClient().setUser((User) input);
        } else if (input instanceof Chat) {
            window.getClient().setChat((Chat) input);
        } else if (input instanceof Result) {
            Result<?> r = (Result<?>) input;
            System.out.println(r);
            long screenID = r.getQuery().getCallerID();
            window.getScreen(screenID).handleSendable(r);
        }
    }

}

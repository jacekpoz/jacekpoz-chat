package com.github.jacekpoz.server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerMain {

    private static List<ChatThread> threads = new ArrayList<>();

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Correct usage: java jacekpozchatserver.jar <port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        boolean listening = true;

        ExecutorService executor = Executors.newCachedThreadPool();

        SwingUtilities.invokeLater(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (listening) {
                    ChatThread t = new ChatThread(new ChatSocket(serverSocket.accept()));
                    threads.add(t);
                    executor.submit(t);
                }
            } catch (IOException e) {
                System.err.println("Couldn't listen on port " + port);
            }
        });
    }
}

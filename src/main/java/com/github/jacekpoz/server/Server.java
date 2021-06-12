package com.github.jacekpoz.server;

import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private ExecutorService executor;
    private @Getter List<ChatThread> threads;

    public Server(ServerSocket ss) throws IOException {
        serverSocket = ss;
        executor = Executors.newCachedThreadPool();
        threads = new ArrayList<>();
    }

    public void start() throws IOException {
        if (executor.isShutdown()) executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            try {
                while (true) {
                    ChatThread thread = new ChatThread(serverSocket.accept(), this);
                    threads.add(thread);
                    executor.submit(thread);
                }
            } catch (IOException e) {
                System.err.println("Couldn't listen on port " + serverSocket.getLocalPort());
            }
        });
    }

    public void stop() throws IOException {
        executor.shutdown();
        serverSocket.close();
    }
}

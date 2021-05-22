package com.github.jacekpoz.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ChatThread extends Thread {

    private ChatSocket clientSocket;

    public ChatThread(ChatSocket s) {
        super("ChatThread");
        clientSocket = s;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String input, output;
            ChatProtocol jcp = new ChatProtocol();

            while ((input = in.readLine()) != null) {
                output = jcp.processMessage(input);
                out.println(output);
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

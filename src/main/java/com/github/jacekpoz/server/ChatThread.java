package com.github.jacekpoz.server;

import com.github.jacekpoz.common.Conversation;
import com.github.jacekpoz.common.Message;
import com.github.jacekpoz.common.UserInfo;
import lombok.Getter;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

public class ChatThread extends Thread {

    private final @Getter Socket clientSocket;
    private final @Getter Server server;
    private @Getter UserInfo currentUser;
    private @Getter Conversation currentChat;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ChatThread(Socket so, Server se) throws IOException {
        super("ChatThread");
        clientSocket = so;
        server = se;
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    public void run() {
        Object input;
        ChatProtocol cp = new ChatProtocol();
        Message output;
        while (true) {
            try {
                input = in.readObject();

                if (input instanceof UserInfo) {
                    currentUser = (UserInfo) input;

                    System.out.println(currentUser);
                    continue;
                }

                if (input instanceof Conversation) {
                    currentChat = (Conversation) input;

                    System.out.println(currentChat);
                    continue;
                }

                output = cp.processMessage((Message) input);

                for (ChatThread ct : server.getThreads())
                    if (!this.equals(ct)) ct.send(output);

            } catch (EOFException e) {
                System.err.println("EOF");
                break;
            } catch (SocketException e) {
                System.out.println("Thread disconnected: " + this);
                e.printStackTrace();
                server.getThreads().remove(this);
                try {
                    clientSocket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendToConversation() {

    }

    public void send(Message m) throws IOException {
        out.writeObject(m);
    }

    @Override
    public String toString() {
        return "ChatThread{" +
                "user=" + currentUser +
                "}";
    }

    // auto generated by intellij
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatThread that = (ChatThread) o;
        return Objects.equals(currentUser, that.currentUser);
    }

}

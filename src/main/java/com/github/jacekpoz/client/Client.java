package com.github.jacekpoz.client;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.server.ChatSocket;

public class Client {

    private ChatSocket clientSocket;
    private ChatWindow window;
    private boolean isLoggedIn;

    public Client(ChatSocket s) {
        clientSocket = s;
        window = new ChatWindow(clientSocket);
        isLoggedIn = false;
    }

    public String getIP() {
        return clientSocket.getInetAddress().getHostName();
    }

    public int getPort() {
        return clientSocket.getPort();
    }

    public ChatWindow getWindow() {
        return window;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

}

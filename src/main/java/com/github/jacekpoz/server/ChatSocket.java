package com.github.jacekpoz.server;

import com.github.jacekpoz.common.User;

import java.io.*;
import java.net.Socket;

public class ChatSocket {

    private final Socket socket;
    private User user;

    public ChatSocket(Socket s) throws IOException {
        socket = s;
        user = new User();
    }

    public Socket getSocket() {
        return socket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        user.initUser(u);
    }

    
}

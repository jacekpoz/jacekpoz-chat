package com.github.jacekpoz.client;

import com.github.jacekpoz.client.gui.ChatWindow;
import com.github.jacekpoz.common.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private @Getter Socket socket;
    private @Getter ChatWindow window;
    private @Getter @Setter UserInfo user;

    public Client(Socket s) {
        socket = s;
        window = new ChatWindow(this);
        user = new UserInfo(-1, "dupa", "dupa dupa");
    }

    public void start() {
        window.start();
    }

    public void stop() throws IOException {
        socket.close();
        window.dispose();
    }

}
